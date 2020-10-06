package hu.kincstar.taskmanager;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FibonacciNumber that = (FibonacciNumber) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "FibonacciNumber{" +
                "value=" + value +
                '}';
    }
}
