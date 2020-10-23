package hu.kincstar.taskmanager.cli;

import java.util.concurrent.Callable;

public class MenuItem<T> {
    String menuSelector;
    String menuText;
    Callable<T> menuAction;

    public MenuItem(String menuSelector, String menuText, Callable<T> menuAction) {
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

    public Callable<T> getMenuAction() {
        return menuAction;
    }
}
