package hu.kincstar.taskmanager.cli;

import hu.kincstar.taskmanager.Task;
import hu.kincstar.taskmanager.TaskManager;

import java.util.*;

public class CliMain {

    private static TaskManager taskManager = new TaskManager();

    private static Scanner scanner = new Scanner(System.in);

    private static List<MenuItem> mainMenu = new ArrayList<>(){
        {add(new MenuItem('A', "Add new task",()->addNewTask()));}
        {add(new MenuItem('L', "List all tasks",()->printAllTasks()));}
        {add(new MenuItem('C', "Change status of task",()->changeStatusOfTask()));}
        {add(new MenuItem('D', "Delete task",()->deleteTask()));}
        {add(new MenuItem('S', "List tasks by status",()->listTasksByStatus()));}
        {add(new MenuItem('U', "List tasks by user",()->listTasksByUser()));}
        {add(new MenuItem('X', "Exit",()->{return;}));}
    };

    private static void listTasksByUser() {
    }

    private static void listTasksByStatus() {
    }

    private static void deleteTask() {
        // TODO
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
        menu.forEach(menuItem -> {
            System.out.println("(" + menuItem.getMenuSelector() + ")" + " " + menuItem.getMenuText());
        });
        printLine();
        System.out.println("Please select an option!");

        Character selection = scanner.nextLine().toUpperCase().charAt(0);
        if (menuContainsSelector(menu, selection)) {
            System.out.println();
            getMenuItemBySelector(menu, selection).menuAction.run();
            return;
        }

        System.out.println();
        System.out.println("Invalid option");
        printMenu(menu);
    }

    private static MenuItem getMenuItemBySelector(List<MenuItem> menu, Character selection) {
        return menu.stream().filter(menuItem -> menuItem.getMenuSelector() == selection).findFirst().orElseThrow();
    }

    private static boolean menuContainsSelector(List<MenuItem> menu, Character selection) {
        return menu.stream().anyMatch(menuItem -> menuItem.getMenuSelector() == selection);
    }

    private static void printLine() {
        System.out.println("--------------------------------");
    }

    public static void printStatusInfo(){
        // TODO print info
    }

}
