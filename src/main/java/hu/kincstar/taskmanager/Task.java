package hu.kincstar.taskmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Task {
    private String user;
    private FibonacciNumber estimatedExecutionTime;
    private TaskStatus status;
    private String description;

    private Task parentTask;

    private Map<Task, RelationType> relatedTasks = new HashMap<>();

    public Task(String user, int estimatedExecutionTime, String description) {
        this.user = user;
        this.estimatedExecutionTime = new FibonacciNumber(estimatedExecutionTime);
        this.description = description;
    }

    public void AddChild(Task task, boolean setBothWay){
        if(hasRelation(task)){
            throw new IllegalArgumentException("Circular relationship found");
        }

        relatedTasks.put(task, RelationType.CHILD);

        if(setBothWay) task.addParent(this, false);
        // TODO ellenőrizni, hogy nincs-e hurok, illetve nincs-e a listában már
        // TODO a szülő kapcsolatot is be kellene állítani
        // TODO előzmény nem lehet szülői és gyerek ágon kapcsolatban és önmaga sem lehet szülő, gyerek ágon, max független
        // TODO szülő gyerek kapcsolat egyik résztvevője és rokonai sem lehetnek más kapcsolatban egymással
    }

    private void addParent(Task task, boolean setBothWay) {
        // TODO
        if(hasRelation(task)){
            throw new IllegalArgumentException("Circular relationship found");
        }

        parentTask = task;
    }

    private boolean hasRelation(Task task) {
        // TODO
        return false;
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

    public Map<Task, RelationType> getRelatedTasks() {
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

    public void setRelatedTasks(Map<Task, RelationType> relatedTasks) {
        this.relatedTasks = relatedTasks;
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
