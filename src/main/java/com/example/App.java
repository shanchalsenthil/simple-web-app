package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.*;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        App app = new App();
        app.runDemo();
    }

    // Demo method
    public void runDemo() {
        List<Integer> numbers = Arrays.asList(10, 20, 30, 40, 50);

        System.out.println("Sum: " + calculateSum(numbers));
        System.out.println("Average: " + calculateAverage(numbers));
        System.out.println("Max: " + findMax(numbers));

        String input = "jenkins";
        System.out.println("Reversed: " + reverseString(input));

        System.out.println("Factorial of 5: " + factorial(5));
    }

    // 1. Calculate Sum
    public int calculateSum(List<Integer> numbers) {
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        return sum;
    }

    // 2. Calculate Average
    public double calculateAverage(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        return calculateSum(numbers) / (double) numbers.size();
    }

    // 3. Find Maximum
    public int findMax(List<Integer> numbers) {
        int max = Integer.MIN_VALUE;
        for (int num : numbers) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    // 4. Reverse String
    public String reverseString(String input) {
        if (input == null) return "";
        return new StringBuilder(input).reverse().toString();
    }

    // 5. Factorial
    public int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Negative number not allowed");
        }
        int result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
