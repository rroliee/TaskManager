package hu.kincstar.taskmanager;

import java.util.ArrayList;
import java.util.List;

public enum TaskStatus {
    NEW, IN_PROGRESS, BLOCKED, DONE;

    public static List<TaskStatus> getPossibleStatusFrom(TaskStatus oldStatus){
        switch(oldStatus){
            case NEW:
                return new ArrayList<>(){{add(IN_PROGRESS);add(BLOCKED);}};
            case BLOCKED:
                return new ArrayList<>(){{add(IN_PROGRESS);}};
            case IN_PROGRESS:
                return new ArrayList<>(){{add(DONE);add(BLOCKED);}};
            default:
                return new ArrayList<>();
        }
    }
}
