package hu.kincstar.taskmanager.cli;

import hu.kincstar.taskmanager.Task;
import hu.kincstar.taskmanager.TaskManager;
import hu.kincstar.taskmanager.enums.TaskStatus;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CliMain {

    private static final TaskManager taskManager = new TaskManager();
    private static Task selectedTask = null;
    private static final Scanner scanner = new Scanner(System.in);

    private static final List<MenuItem> mainMenu = new ArrayList<>(){
        {add(new MenuItem("A", "Add new task", CliMain::addNewTask));}
        {add(new MenuItem("L", "List all tasks", CliMain::printAllTasks));}
        {add(new MenuItem("C", "Change status of task", CliMain::changeStatusOfTask));}
        {add(new MenuItem("D", "Delete task", CliMain::deleteTask));}
        {add(new MenuItem("S", "List tasks by status", CliMain::listTasksByStatus));}
        {add(new MenuItem("U", "List tasks by user", CliMain::listTasksByUser));}
        {add(new MenuItem("X", "Exit",()->{}));}
    };

    private static void listTasksByUser() {
        List<MenuItem> userSelectionMenu = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        taskManager.getDistinctUsers().forEach(user-> userSelectionMenu.add(
                new MenuItem(Integer.toString(count.getAndIncrement()),
                        user,
                        ()->printTasks(taskManager.getTasksByUser(user)))));
        printMenu(userSelectionMenu);
    }

    private static void listTasksByStatus() {
        List<MenuItem> statusSelectionMenu = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        Arrays.stream(TaskStatus.values()).forEach(status-> statusSelectionMenu.add(
                new MenuItem(Integer.toString(count.getAndIncrement()),
                        status.toString(),
                        ()->printTasks(taskManager.getTasksByStatus(status)))));
        printMenu(statusSelectionMenu);
    }

    private static void deleteTask() {
        selectTask(CliMain::deleteSelectedTask);
    }

    private static void selectTask(Runnable callBack) {
        List<MenuItem> taskSelectionMenu = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();

        taskManager.getTasks().forEach(task -> taskSelectionMenu.add(
                new MenuItem(Integer.toString(counter.getAndIncrement()),
                        task.toString(),
                        ()-> selectedTask = task)));

        printMenu(taskSelectionMenu);
        callBack.run();
    }

    private static void deleteSelectedTask() {
        if(selectedTask == null){
            throw new IllegalStateException("No selected task for delete operation");
        }
        try {
            taskManager.deleteTask(selectedTask);
            System.out.println("Task deleted");
        }catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
        printMenu(mainMenu);
    }

    private static void changeStatusOfTask() {
        selectTask(CliMain::listPossibleStatusChangesForSelectedTask);
    }

    private static void listPossibleStatusChangesForSelectedTask() {
        //TODO
        if(selectedTask == null){
            throw new IllegalStateException("No selected task for change status operation");
        }
        List<TaskStatus> possibleStatusChanges = selectedTask.getPossibleStatusChanges();
        if(possibleStatusChanges.size() == 0){
            System.out.println("Status cannot be changed");
            printMenu(mainMenu);
            return;
        }

        List<MenuItem> statusChangeMenu = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        possibleStatusChanges.forEach(taskStatus -> {
            statusChangeMenu.add(new MenuItem(Integer.toString(counter.getAndIncrement()), taskStatus.name(), () -> {
                taskManager.changeTaskStatus(selectedTask, taskStatus);
                System.out.println("Status changed");
                printMenu(mainMenu);
            }));
        });

        printMenu(statusChangeMenu);
    }

    private static void printAllTasks() {
        printTasks(taskManager.getTasks());
    }

    private static void printTasks(List<Task> tasks) {
        if(tasks == null || tasks.size() == 0){
            System.out.println("No tasks found");
        }else {
            TaskManager.printTaskList(tasks);
        }
        printMenu(mainMenu);
    }

    public static void addNewTask(){
        System.out.println("Please enter task details");
        String user = readStringFromConsole("user");
        String description = readStringFromConsole("description");
        int estimatedExecutionTime = readIntegerFromConsole("estimated execution time");

        try {
            Task newTask = new Task(user, estimatedExecutionTime, description);
            taskManager.addTask(newTask);
            System.out.println("Task added successfully");
        }catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
        printMenu(mainMenu);
    }

    private static String readStringFromConsole(String fieldName) {
        System.out.println(fieldName + ": ");
        String value = scanner.nextLine();
        System.out.println("Given " + fieldName + " is: " + value);
        return value;
    }

    private static int readIntegerFromConsole(String fieldName) {
        int value = 0;
        boolean successfullRead = false;
        while(!successfullRead) {
            try {
                System.out.println(fieldName + ":");

                if(scanner.hasNextInt()){
                    value = scanner.nextInt();
                    successfullRead = true;
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

    public static void main(String[] args) {
        printMenu(mainMenu);
    }

    public static void printMenu(List<MenuItem> menu){
        printLine();
        menu.forEach(CliMain::printMenuLine);
        printLine();
        System.out.println("Please select an option!");

        String selection = scanner.nextLine().toUpperCase();
        if (menuContainsSelector(menu, selection)) {
            System.out.println();
            getMenuItemBySelector(menu, selection).getMenuAction().run();
            return;
        }

        System.out.println();
        System.out.println("Invalid option");
        printMenu(menu);
    }

    private static MenuItem getMenuItemBySelector(List<MenuItem> menu, String selection) {
        return menu.stream().filter(menuItem -> menuItem.getMenuSelector().equals(selection)).findFirst().orElseThrow();
    }

    private static boolean menuContainsSelector(List<MenuItem> menu, String selection) {
        return menu.stream().anyMatch(menuItem -> menuItem.getMenuSelector().equals(selection));
    }

    private static void printLine() {
        System.out.println("--------------------------------");
    }

    public static void printStatusInfo(){
        // TODO print info
    }

    private static void printMenuLine(MenuItem menuItem) {
        System.out.println("(" + menuItem.getMenuSelector() + ")" + " " + menuItem.getMenuText());
    }
}
