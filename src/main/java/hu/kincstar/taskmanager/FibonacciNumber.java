package hu.kincstar.taskmanager;

public class FibonacciNumber {

    private final int value;


    public FibonacciNumber(int value) {
        if(!isFibonacciNumber(value)){
            throw new IllegalArgumentException("Value is not a Fibonacci number");
        }

        this.value = value;
    }

    private boolean isFibonacciNumber(int value) {
        return isPerfectSquare(5*value*value + 4) ||
                isPerfectSquare(5*value*value - 4);
    }

    private boolean isPerfectSquare(int value) {
        int s = (int) Math.sqrt(value);
        return (s*s == value);
    }

    public int getValue() {
        return value;
    }
}
