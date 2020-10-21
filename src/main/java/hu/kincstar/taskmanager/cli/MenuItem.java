package hu.kincstar.taskmanager.cli;

public class MenuItem {
    char menuSelector;
    String menuText;
    Runnable menuAction;

    public MenuItem(char menuSelector, String menuText, Runnable menuAction) {
        this.menuSelector = menuSelector;
        this.menuText = menuText;
        this.menuAction = menuAction;
    }

    public char getMenuSelector() {
        return menuSelector;
    }

    public String getMenuText() {
        return menuText;
    }

    public Runnable getMenuAction() {
        return menuAction;
    }
}
