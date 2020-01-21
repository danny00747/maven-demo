package test.java.com.in28minutes.maven;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.App;

class AppTest {
    @Test
    public void testApp() {
        assertEquals(0, new App().calculateSomething());
    }
}
