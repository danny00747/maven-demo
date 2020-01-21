package test.java.com.in28minutes.maven;

import static org.junit.Assert.*;

import main.java.com.in28minutes.maven.App;
import org.junit.Test;

public class AppTest 
{
	@Test
	public void testApp()
    {
        assertEquals(0,new App().calculateSomething());
    }
}
