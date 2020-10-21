package hu.kincstar.taskmanager;

import hu.kincstar.taskmanager.enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        if (tasks.contains(task)) throw new IllegalArgumentException("Task already added");

        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void changeTaskStatus(Task task, TaskStatus newStatus) {
        if (!tasks.contains(task)) throw new IllegalArgumentException(("Unknown task"));

        task.setStatus(newStatus);
    }

    public void deleteTask(Task task) {
        if (!tasks.contains(task)) throw new IllegalArgumentException(("Unknown task"));
        if(task.getChildren().stream().anyMatch(child -> child.getStatus()!= TaskStatus.DONE))
            throw new IllegalArgumentException(("Task has open sub task(s)"));

        tasks.remove(task);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return tasks.stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByUser(String user) {
        return tasks.stream()
                .filter(task -> task.getUser().equals(user))
                .collect(Collectors.toList());
    }

    public List<String> getDistinctUsers() {
        return tasks.stream()
                .map(Task::getUser)
                .distinct()
                .collect(Collectors.toList());
    }

    public static void printTaskList(List<Task> tasks) {
        for (Task task : tasks) System.out.println(task.toString());
    }
}
