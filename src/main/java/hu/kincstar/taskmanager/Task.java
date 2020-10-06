package hu.kincstar.taskmanager;

import java.util.*;

public class Task {
    private String user;
    private FibonacciNumber estimatedExecutionTime;
    private TaskStatus status;
    private String description;

    private Task parentTask;

    private Map<RelationType, Set<Task>> relatedTasks = new HashMap<>();

    public Task(String user, int estimatedExecutionTime, String description) {
        this.user = user;
        this.estimatedExecutionTime = new FibonacciNumber(estimatedExecutionTime);
        this.description = description;

        // relatedTasks inicializálása minden RelationType-ra
        for (int i = 0; i < RelationType.values().length; i++)
            relatedTasks.put(RelationType.values()[i], new HashSet<>());
    }

    /**
     * Felveszi a paraméterként kapott child taskot gyereknek, majd beállítja önmagát szülőnek.
     * Illegális gyerek paraméter esetén IllegalArgumentException-t dob.
     * @param child Gyerek elem felvehető, ha gyerek nem azonos az objektummal, a gyereknek még nincs szülője és a
     *              gyerek nem azonos a gyökér elemmel.
     */
    public void AddChild(Task child){

        if(child == this)
            throw new IllegalArgumentException("Child cannot be self");
        if(child.getParentTask() != null){
            throw new IllegalArgumentException("Child already has a parent");
        }
        if(getRoot() == child){
            throw new IllegalArgumentException("Child is the root of self");
        }

        relatedTasks.get(RelationType.CHILD).add(child);
        child.addParent(this);
    }

    private void addParent(Task parent) {
        if(parentTask != null){
            throw new IllegalStateException("Task has already a parent");
        }
        parentTask = parent;
    }

    private Task getRoot() {
        if(parentTask != null){
            return parentTask.getRoot();
        }
        return this;
    }


    /**
     * Ellenőrzi, hogy megadott feladat kapcsolódik-e valamilyen szűlő-gyerek kapcsolattal a saját gráfunkhoz.
     * @param task Ellenőrizendő feladat.
     * @return  Igaz, ha van kapcsolat, hamis egyébként.
     */
    private boolean hasRelationWith(Task task) {
        if(this == task) return true;

        return  relatedTasks.get(RelationType.CHILD).stream()
                .anyMatch(child-> child.hasRelationWith(task)) ||
                (parentTask != null && parentTask.hasRelationWith(task));
    }

    /**
     * Felveszi a paraméterként kapott precedessor taskot előfeltételnek, majd beállítja önmagát követő feladatnak.
     * @param precedessor Előfeltétel elem felvehető, ha előfeltétel nem azonos az objektummal, ha még nincs felvéve
     *                    előfeltételnek
     */
    public void AddPrecedessor(Task precedessor){

        if(precedessor == this)
            throw new IllegalArgumentException("Precedessor cannot be self");

        relatedTasks.get(RelationType.PREDECESSOR).add(precedessor);
        precedessor.
    }

    private void addFollower(Task follower) {
        if(relatedTasks.get(RelationType.FOLLOWER).contains(follower)){
            throw new IllegalStateException("Follower has already been added");
        }
        relatedTasks.get(RelationType.FOLLOWER).add(follower);
    }

    public List<TaskStatus> getPossibleStatusChanges(){
        // TODO
        return  null;
    }

    public String getUser() {
        return user;
    }

    public int getEstimatedExecutionTime() {
        return estimatedExecutionTime.getValue();
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public Map<RelationType, Set<Task>> getRelatedTasks() {
        return relatedTasks;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setEstimatedExecutionTime(int estimatedExecutionTime) {
        this.estimatedExecutionTime = new FibonacciNumber(estimatedExecutionTime);
    }

    public void setStatus(TaskStatus status) {
        // TODO
        // itt ellenőrizni kellene a státuszváltozást
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return estimatedExecutionTime == task.estimatedExecutionTime &&
                Objects.equals(user, task.user) &&
                status == task.status &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, estimatedExecutionTime, status, description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "user='" + user + '\'' +
                ", estimatedExecutionTime=" + estimatedExecutionTime +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
