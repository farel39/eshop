package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public class OrderHistoryFunctionalTest extends BaseFunctionalTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    /**
     * Helper method to ensure a test product exists.
     * If no products are found, create one using the actual instantiation.
     */
    private void ensureTestProductExists() {
        List<Product> products = productService.findAll();
        if (products.isEmpty()) {
            Product product1 = new Product();
            product1.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
            product1.setName("Sampo Cap Bambang");
            product1.setQuantity(2);
            productService.create(product1);
        }
    }

    /**
     * Helper method to create a test order for the given author.
     */
    private Order createTestOrder(String author) {
        // Ensure at least one product exists.
        ensureTestProductExists();
        List<Product> products = productService.findAll();
        // Use the first product for simplicity.
        Product product = products.get(0);
        Order order = new Order(UUID.randomUUID().toString(), List.of(product), System.currentTimeMillis(), author);
        return orderService.createOrder(order);
    }

    /**
     * Test that the Order History Form page is displayed.
     */
    @Test
    public void testGetOrderHistoryForm(ChromeDriver driver) {
        // Navigate to the Order History Form page.
        driver.get(baseUrl + "/order/history");

        // Verify the page by checking a header.
        // (Assuming your OrderHistoryForm view renders an <h1> with "Order History Form")
        WebElement header = driver.findElement(By.tagName("h1"));
        assertEquals("View Order History", header.getText(), "The Order History Form header should be displayed");
    }

    /**
     * Test that submitting the Order History Form shows orders for the given author.
     */
    @Test
    public void testShowOrderHistory(ChromeDriver driver) {
        String author = "TestAuthorOrderHistory";

        // Create a test order for this author using the service layer.
        createTestOrder(author);

        // Navigate to the Order History Form page.
        driver.get(baseUrl + "/order/history");

        // Fill out the form with the author's name.
        // (Assuming the input field has id "name")
        WebElement nameInput = driver.findElement(By.id("name"));
        nameInput.sendKeys(author);

        // Submit the form.
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Verify that the resulting page shows the Order History.
        // (Assuming the view renders an <h1> with "Order History")
        WebElement header = driver.findElement(By.tagName("h1"));
        assertEquals("Order History", header.getText(), "The Order History header should be displayed");

        // Verify that the order with the given author appears on the page.
        WebElement orderAuthorElement = driver.findElement(
                By.xpath("//p[contains(., 'Author:')]/span[text()='" + author + "']")
        );
        assertEquals(author, orderAuthorElement.getText(), "The order with the specified author should be listed");
    }
}
