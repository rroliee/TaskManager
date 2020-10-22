package hu.kincstar.taskmanager.cli;

public class MenuItem {
    String menuSelector;
    String menuText;
    Runnable menuAction;

    public MenuItem(String menuSelector, String menuText, Runnable menuAction) {
        this.menuSelector = menuSelector.toUpperCase();
        this.menuText = menuText;
        this.menuAction = menuAction;
    }

    public String getMenuSelector() {
        return menuSelector;
    }

    public String getMenuText() {
        return menuText;
    }

    public Runnable getMenuAction() {
        return menuAction;
    }
}
