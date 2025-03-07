package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public class CreateOrderFunctionalTest extends BaseFunctionalTest {

    // Helper method to create a product
    private void createProduct(ChromeDriver driver) {
        // Navigate to the Create New Product page
        driver.get(baseUrl + "/product/create");

        // Fill out the product form
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        nameInput.sendKeys("Test Product");

        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        quantityInput.sendKeys("10");

        // Submit the product form
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Verify that the new product is in the list
        WebElement productNameCell = driver.findElement(By.xpath("//td[text()='Test Product']"));
        WebElement productQuantityCell = driver.findElement(By.xpath("//td[text()='10']"));

        assertEquals("Test Product", productNameCell.getText(), "Product name should be displayed in the list");
        assertEquals("10", productQuantityCell.getText(), "Product quantity should be displayed in the list");
    }

    @Test
    void createOrder_andVerify(ChromeDriver driver) {
        // Create a product first
        createProduct(driver);

        // Navigate to the Create Order page
        driver.get(baseUrl + "/order/create");

        // Fill out the form: set the author
        WebElement authorInput = driver.findElement(By.id("author"));
        authorInput.sendKeys("Test Author");

        // Select a product checkbox (assumes that at least one product exists)
        WebElement productCheckbox = driver.findElement(By.name("productIds"));
        productCheckbox.click();

        // Submit the form
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Verify that after submission, the Create Order page is re-displayed
        WebElement header = driver.findElement(By.tagName("h1"));
        assertEquals("Order Created Successfully", header.getText());

    }

    @Test
    void createOrder_withoutProduct_andVerifyError(ChromeDriver driver) {
        // Create a product first to ensure the product list is rendered
        createProduct(driver);

        // Navigate to the Create Order page
        driver.get(baseUrl + "/order/create");

        // Fill out the form: set the author
        WebElement authorInput = driver.findElement(By.id("author"));
        authorInput.sendKeys("Test Author Without Product");

        // Do NOT select any product checkbox

        // Submit the form
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Verify that the Create Order page is re-displayed (header remains "Create Order")
        WebElement header = driver.findElement(By.tagName("h1"));
        assertEquals("Create Order", header.getText());

        // Verify that the error message is displayed in the page
        WebElement errorDiv = driver.findElement(By.xpath("//*[contains(text(),'Please select at least one product.')]"));
        assertEquals("Please select at least one product.", errorDiv.getText());
    }
}
