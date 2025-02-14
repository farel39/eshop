package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest extends BaseFunctionalTest{


    @Test
    void createProduct_andVerifyInList(ChromeDriver driver) throws Exception {
        // Navigate to the Create New Product page
        driver.get(baseUrl + "/product/create");

        // Fill out the form
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        nameInput.sendKeys("Test Product");

        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        quantityInput.sendKeys("10");

        // Submit the form
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Wait for the Product List page to load
        WebElement productNameCell = driver.findElement(By.xpath("//td[text()='Test Product']"));
        WebElement productQuantityCell = driver.findElement(By.xpath("//td[text()='10']"));

        // Verify that the new product is in the list
        assertEquals("Test Product", productNameCell.getText(), "Product name should be displayed in the list");
        assertEquals("10", productQuantityCell.getText(), "Product quantity should be displayed in the list");
    }
}
