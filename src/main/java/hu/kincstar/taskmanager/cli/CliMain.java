package hu.kincstar.taskmanager.cli;

import hu.kincstar.taskmanager.Task;
import hu.kincstar.taskmanager.TaskManager;

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
    }

    private static void listTasksByStatus() {
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
        // TODO
    }

    private static void printAllTasks() {
        TaskManager.printTaskList(taskManager.getTasks());

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
