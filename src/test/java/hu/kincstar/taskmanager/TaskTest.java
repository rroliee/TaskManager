package hu.kincstar.taskmanager;

import org.junit.Assert;
import org.junit.Test;

public class TaskTest {
    @Test
    public void AddChildTest(){
        Task parentTask = new Task("user1", 1, "short description");
        Task childTask = new Task("user1", 2, "short description");

        parentTask.addChild(childTask);

        Assert.assertEquals(childTask, parentTask.getChildren().get(0));
        Assert.assertEquals(parentTask, childTask.getParentTask());
    }

    @Test(expected = IllegalArgumentException.class)
    public void AddChildCircleTest(){
        Task parentTask = new Task("user1", 1, "short description");
        Task childTask = new Task("user1", 2, "short description");

        parentTask.addChild(childTask);
        childTask.addChild(parentTask);
    }

    @Test
    public void AddPredecessorTest(){
        Task parentTask = new Task("user1", 1, "short description");
        Task predecessorTask = new Task("user1", 2, "short description");

        parentTask.addPredecessor(predecessorTask);

        Assert.assertEquals(predecessorTask, parentTask.getPredecessors().get(0));
        Assert.assertEquals(parentTask, predecessorTask.getFollowers().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void AddPredecessorCircleTest(){
        Task parentTask = new Task("user1", 1, "short description");
        Task predecessorTask = new Task("user1", 2, "short description");

        parentTask.addPredecessor(predecessorTask);
        predecessorTask.addPredecessor(parentTask);
    }


}
