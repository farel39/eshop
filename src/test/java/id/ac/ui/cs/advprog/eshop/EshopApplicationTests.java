package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

@SpringBootTest
class EshopApplicationTests {

	@Test
	void contextLoads() {
		// This method is intentionally left empty.
		// Nested Explanation:
		// - This is a standard test in Spring Boot applications.
		// - It verifies that the application context loads without any issues.
		// - If the application fails to start due to configuration issues, this test will fail.
	}

	@Test
	void testMain() {
		assertDoesNotThrow(() -> EshopApplication.main(new String[]{}));
	}

}
