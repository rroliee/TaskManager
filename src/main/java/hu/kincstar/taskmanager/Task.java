package hu.kincstar.taskmanager;

import hu.kincstar.taskmanager.enums.RelationType;
import hu.kincstar.taskmanager.enums.TaskStatus;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Task implements Serializable {
    private final int id;
    private String user;
    private FibonacciNumber estimatedExecutionTime;
    private TaskStatus status;
    private String description;

    private Map<RelationType, Set<Task>> relatedTasks = new HashMap<>();

    public Task(int id, String user, int estimatedExecutionTime, String description) {
        this.id = id;
        this.user = user;
        try {
            this.estimatedExecutionTime = new FibonacciNumber(estimatedExecutionTime);
        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Estimated execution time is not a fibonacci number");
        }
        this.description = description;
        this.status = TaskStatus.NEW;

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
    public void addChild(Task child){

        if(child == this)
            throw new IllegalArgumentException("Child cannot be self");
        if(child.getParentTask() != null){
            throw new IllegalArgumentException("Child has already a parent");
        }
        if(getRoot() == child){
            throw new IllegalArgumentException("Child is the root of its new parent");
        }

        relatedTasks.get(RelationType.CHILD).add(child);
        child.addParent(this);
    }

    private void addParent(Task parent) {
        if(hasParent()){
            throw new IllegalStateException("Task has already a parent");
        }
        relatedTasks.get(RelationType.PARENT).add(parent);
    }

    private boolean hasParent() {
        return !relatedTasks.get(RelationType.PARENT).isEmpty();
    }

    /**
     * Visszaadja a feladat fa gyökér elemét, a szülő elemek mentén.
     * @return A feladat fa gyökér eleme.
     */
    private Task getRoot() {
        if(hasParent()){
            return getParentTask().getRoot();
        }
        return this;
    }

    /**
     * Felveszi a paraméterként kapott precedessor taskot előfeltételnek, majd beállítja önmagát követő feladatnak.
     * @param predecessor Előfeltétel elem felvehető, ha előfeltétel nem azonos az objektummal, ha nem tartozik az
     *                    objektum követő feladatai (parent és follower ágak) közé.
     */
    public void addPredecessor(Task predecessor){
        if(predecessor == this)
            throw new IllegalArgumentException("Predecessor cannot be self");
        if(isFollowerRelatedTask(predecessor))
            throw new IllegalArgumentException("Predecessor cannot be a following task");

        relatedTasks.get(RelationType.PREDECESSOR).add(predecessor);
        predecessor.addFollower(this);
    }

    /**
     * Megnézi, hogy a paraméter task egyezik-e az objektummal, illetve az objektum bármelyik követő elemével.
     * @param task Ezt a taskot keressük.
     * @return Igaz, ha ez a feladat, vagy bármelyik követő feladat megegyezik a paraméter feladattal, egyébként hamis.
     */
    private boolean isFollower(Task task) {
        if(task == this) return true;
        return isFollowerRelatedTask(task);
    }

    /**
     * Megnézi, hogy a paraméter task egyezik-e az objektum bármelyik követő elemével.
     * @param task Ezt a taskot keressük.
     * @return Igaz, ha bármelyik követő feladat megegyezik a paraméter feladattal, egyébként hamis.
     */
    private boolean isFollowerRelatedTask(Task task) {
        return relatedTasks.entrySet().stream()
                // összes követő kapcsolat (PARENT, FOLLOWER)
                .filter(keyValuePair -> keyValuePair.getKey() == RelationType.PARENT
                        || keyValuePair.getKey() == RelationType.FOLLOWER)
                // csak a set-ek
                .map(Map.Entry::getValue)
                // set-ek össze mergelése
                .flatMap(Set::stream)
                // ha bármelyik azt mondja magáról, hogy követő task
                .anyMatch(followerTask -> followerTask.isFollower(task));
    }

    private void addFollower(Task follower) {
        if(relatedTasks.get(RelationType.FOLLOWER).contains(follower)){
            throw new IllegalStateException("Follower has already been added");
        }
        relatedTasks.get(RelationType.FOLLOWER).add(follower);
    }

    public List<TaskStatus> getPossibleStatusChanges(){
        List<TaskStatus> possibleStatuses = TaskStatus.getPossibleStatusFrom(status);
        // Egy feladatot ne lehessen DONE státuszba helyezni, amíg a CHILD relációban lévő feladatok nincsenek mind DONE státuszban
        if(getChildren().stream().anyMatch(child -> child.getStatus() != TaskStatus.DONE)){
            possibleStatuses.remove(TaskStatus.DONE);
        }
        // Egy feladatot ne lehessen IN_PROGRESS státuszba helyezni amíg a PRECEDESSOR típusú linkelt feladatok nincsenek DONE státuszban
        if(getPredecessors().stream().anyMatch(predecessor -> predecessor.getStatus() != TaskStatus.DONE)){
            possibleStatuses.remove(TaskStatus.IN_PROGRESS);
        }
        return  possibleStatuses;
    }

    public int getId() {
        return id;
    }

    public List<Task> getChildren(){
        return getRelatedTasks(RelationType.CHILD);
    }

    public List<Task> getPredecessors(){
        return getRelatedTasks(RelationType.PREDECESSOR);
    }

    public List<Task> getFollowers(){
        return getRelatedTasks(RelationType.FOLLOWER);
    }

    private List<Task> getRelatedTasks(RelationType relationType){
        return new ArrayList<>(relatedTasks.get(relationType));
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
        return relatedTasks.get(RelationType.PARENT).stream().findFirst().orElse(null);
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

    public void setStatus(TaskStatus newStatus) {
        if(!getPossibleStatusChanges().contains(newStatus)){
            throw new IllegalArgumentException("Status change not acceptable");
        }
        this.status = newStatus;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // OVERRIDE-ok

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
        StringBuilder sb = new StringBuilder("(")
                .append(id).append(") ").append("Task{")
                .append("user='").append(user).append('\'')
                .append(", estimatedExecutionTime=").append(estimatedExecutionTime)
                .append(", status=").append(status)
                .append(", description='").append(description).append('\'');
        relatedTasks.forEach((relation, tasks) -> sb.append(", ")
                .append(relation).append("='")
                .append(
                        tasks.size() == 0 ?
                                "NA" :
                                tasks.stream().map(task -> String.valueOf(task.getId())).collect(Collectors.joining(", "))
                )
                .append('\''));
        sb.append('}');

        return sb.toString();
    }
}
