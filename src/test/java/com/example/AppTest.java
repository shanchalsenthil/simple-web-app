package com.example;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    App app = new App();

    @Test
    void testSum() {
        assertEquals(60, app.calculateSum(Arrays.asList(10,20,30)));
    }

    @Test
    void testAverage() {
        assertEquals(20.0, app.calculateAverage(Arrays.asList(10,20,30)));
    }

    @Test
    void testMax() {
        assertEquals(30, app.findMax(Arrays.asList(10,20,30)));
    }

    @Test
    void testReverse() {
        assertEquals("olleh", app.reverseString("hello"));
    }

    @Test
    void testFactorial() {
        assertEquals(120, app.factorial(5));
    }
}
