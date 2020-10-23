package hu.kincstar.taskmanager.cli;

import hu.kincstar.taskmanager.Task;
import hu.kincstar.taskmanager.TaskManager;
import hu.kincstar.taskmanager.enums.TaskStatus;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CliMain {

    private static final TaskManager taskManager = new TaskManager();
    private static final Scanner scanner = new Scanner(System.in);

    private static final List<MenuItem<Object>> mainMenu = new ArrayList<>(){
        {add(new MenuItem<>("A", "Add new task", CliMain::addNewTask));}
        {add(new MenuItem<>("D", "Delete task", CliMain::deleteTask));}
        {add(new MenuItem<>("C", "Change status of task", CliMain::changeStatusOfTask));}
        {add(new MenuItem<>("AC", "Add child to task", CliMain::addChildToTask));}
        {add(new MenuItem<>("AP", "Add predecessor to task", CliMain::addPredecessorToTask));}
        {add(new MenuItem<>("L", "List all tasks", CliMain::printAllTasks));}
        {add(new MenuItem<>("S", "List tasks by status", CliMain::listTasksByStatus));}
        {add(new MenuItem<>("U", "List tasks by user", CliMain::listTasksByUser));}
        {add(new MenuItem<>("X", "Exit",()-> {System.exit(0); return null;}));}
    };

    public static void main(String[] args) {
        while (true)
            printMenu(mainMenu, "Main menu");
    }

    public static Task addNewTask(){
        System.out.println("Please enter task details");
        String user = readStringFromConsole("user");
        String description = readStringFromConsole("description");
        int estimatedExecutionTime = readIntegerFromConsole("estimated execution time");

        try {
            Task newTask = new Task(user, estimatedExecutionTime, description);
            taskManager.addTask(newTask);
            System.out.println("Task added successfully");
            return newTask;
        }catch(IllegalArgumentException ex){
            System.err.println(ex.getMessage());
            return null;
        }
    }

    private static boolean deleteTask() {
        Task taskToDelete = selectTask("Select task to delete");
        return deleteSelectedTask(taskToDelete);
    }

    private static boolean deleteSelectedTask(Task taskToDelete) {
        // TODO itt kell hibát kezelni?
        try {
            if(taskToDelete == null){
                throw new IllegalStateException("No selected task for delete operation");
            }
            taskManager.deleteTask(taskToDelete);
            System.out.println("Task deleted");
            return true;
        }catch(IllegalArgumentException|IllegalStateException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    private static boolean changeStatusOfTask() {
        Task task = selectTask("Select task for status change");
        TaskStatus newStatus = selectPossibleStatusChangesForSelectedTask(task);
        // TODO try catch
        boolean statusChanged = false;

        if(newStatus == null){
            System.out.println("Status cannot be changed");
        } else {
            taskManager.changeTaskStatus(task, newStatus);
            System.out.println("Status changed");
            statusChanged = true;
        }
        return statusChanged;
    }

    private static TaskStatus selectPossibleStatusChangesForSelectedTask(Task task) {
        if(task == null){
            throw new IllegalStateException("No selected task for change status operation");
        }
        List<TaskStatus> possibleStatusChanges = task.getPossibleStatusChanges();
        if(possibleStatusChanges.size() == 0){
            return null;
        }

        List<MenuItem<TaskStatus>> statusChangeMenu = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        possibleStatusChanges.forEach(taskStatus -> statusChangeMenu.add(
                new MenuItem<>(Integer.toString(counter.getAndIncrement()),
                        taskStatus.name(),
                        () -> taskStatus)));

        return printMenu(statusChangeMenu, "Select new status");
    }

    private static boolean addChildToTask() {
        Task parentTask = selectTask("Select parent task");
        Task childTask = selectTask("Select child task");

        parentTask.addChild(childTask);
        // TODO hibakezelés
        System.out.println("Child added");
        return true;
    }

    private static Task selectTask(String title) {
        List<MenuItem<Task>> taskSelectionMenu = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();

        taskManager.getTasks().forEach(task -> taskSelectionMenu.add(
                new MenuItem<>(Integer.toString(counter.getAndIncrement()),
                        task.toString(),
                        () -> task)));

        return printMenu(taskSelectionMenu, title);
    }

    private static boolean addPredecessorToTask() {
        Task followerTask = selectTask("Select follower task");
        Task predecessorTask = selectTask("Select predecessor task");

        followerTask.addPredecessor(predecessorTask);
        // TODO hibakezelés
        System.out.println("Predecessor added");
        return true;
    }

    private static List<Task> printAllTasks() {
        List<Task> tasks = taskManager.getTasks();
        printTasks(tasks);
        return tasks;
    }

    private static List<Task> listTasksByStatus() {
        List<MenuItem<List<Task>>> statusSelectionMenu = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        Arrays.stream(TaskStatus.values()).forEach(status-> statusSelectionMenu.add(
                new MenuItem<>(Integer.toString(count.getAndIncrement()),
                        status.toString(),
                        ()->{
                            List<Task> tasksByStatus = taskManager.getTasksByStatus(status);
                            printTasks(tasksByStatus);
                            return tasksByStatus;
                        })));
        return printMenu(statusSelectionMenu, "Select status");
    }

    private static List<Task> listTasksByUser() {
        List<MenuItem<List<Task>>> userSelectionMenu = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        taskManager.getDistinctUsers().forEach(user-> userSelectionMenu.add(
                new MenuItem<>(Integer.toString(count.getAndIncrement()),
                        user,
                        () -> {
                            List<Task> tasksByUser = taskManager.getTasksByUser(user);
                            printTasks(tasksByUser);
                            return tasksByUser;
                        })));
        return printMenu(userSelectionMenu, "Select user");
    }

    private static void printTasks(List<Task> tasks) {
        if(tasks == null || tasks.size() == 0){
            System.out.println("No tasks found");
        }else {
            TaskManager.printTaskList(tasks);
        }
    }




    // MENU things

    private static String readStringFromConsole(String fieldName) {
        System.out.println(fieldName + ": ");
        String value = scanner.nextLine();
        System.out.println("Given " + fieldName + " is: " + value);
        return value;
    }

    private static int readIntegerFromConsole(String fieldName) {
        int value = 0;
        boolean successfulRead = false;
        while(!successfulRead) {
            try {
                System.out.println(fieldName + ":");

                if(scanner.hasNextInt()){
                    value = scanner.nextInt();
                    successfulRead = true;
                }else{
                    System.out.println("Invalid number");
                }
                scanner.nextLine();
            } catch (NoSuchElementException ex) {
                System.out.println("Please enter a valid number");
            }
        }
        return value;
    }

    public static <T> T printMenu(List<MenuItem<T>> menu, String title){
        do {
            if(title != null && title.trim().length()> 0) {
                printLine();
                System.out.println(title);
            }
            printLine();
            menu.forEach(CliMain::printMenuLine);
            printLine();
            System.out.println("Please select an option!");

            String selection = scanner.nextLine().toUpperCase();
            if (menuContainsSelector(menu, selection)) {
                System.out.println();
                try {
                    return getMenuItemBySelector(menu, selection).getMenuAction().call();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            System.out.println();
            System.out.println("Invalid option");
        }while(true);
    }

    private static <T> MenuItem<T> getMenuItemBySelector(List<MenuItem<T>> menu, String selection) {
        return menu.stream().filter(menuItem -> menuItem.getMenuSelector().equals(selection)).findFirst().orElseThrow();
    }

    private static <T> boolean menuContainsSelector(List<MenuItem<T>> menu, String selection) {
        // TODO minek kell a T ide?
        return menu.stream().anyMatch(menuItem -> menuItem.getMenuSelector().equals(selection));
    }

    private static void printLine() {
        System.out.println("--------------------------------");
    }

    private static void printMenuLine(MenuItem menuItem) {
        System.out.println("(" + menuItem.getMenuSelector() + ")" + " " + menuItem.getMenuText());
    }
}
