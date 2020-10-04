package hu.kincstar.taskmanager;

import org.junit.Assert;
import org.junit.Test;

public class FibonacciNumberTest {
    private static final int[] fibonacciNumbers = new int[]{0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765};

    @Test
    public void fibonacciNumberTest(){
        for (int fibonacciNumber : fibonacciNumbers) {
            FibonacciNumber fn = new FibonacciNumber(fibonacciNumber);
            Assert.assertEquals(fibonacciNumber, fn.getValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongFibonacciNumberTest(){
        FibonacciNumber fn = new FibonacciNumber(4);
    }
}

