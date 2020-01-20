import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class AppTest {
	
	@Test
	public void testApp() {
		assertEquals(0, new App().calculateSomething());
	}
}
