package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mock;

@SpringBootTest
class EshopApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testMain() {
		SpringApplication mockSpringApp = mock(SpringApplication.class);
		EshopApplication.main(new String[]{});
	}

}
