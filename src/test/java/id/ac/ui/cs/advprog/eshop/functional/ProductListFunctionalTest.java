package id.ac.ui.cs.advprog.eshop.functional;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductListFunctionalTest extends BaseFunctionalTest {

    @Test
    void productList_displaysCorrectNumberOfItems(ChromeDriver driver) {
        // Delete all existing products
        deleteAllProducts(driver);

        // Create two products
        createProduct(driver, "Product 1", "5");
        createProduct(driver, "Product 2", "10");

        // Navigate to the product list page
        driver.get(baseUrl + "/product/list");

        // Verify the number of items in the list
        int numberOfProducts = driver.findElements(By.xpath("//tbody/tr")).size();
        assertEquals(2, numberOfProducts, "The product list should display 2 items");
    }

    @Test
    void deleteProduct_removesProductFromList(ChromeDriver driver) {
        // Delete all existing products
        deleteAllProducts(driver);

        // Create a product
        String productName = "Test Product";
        String productQuantity = "15";
        createProduct(driver, productName, productQuantity);

        // Navigate to the product list page
        driver.get(baseUrl + "/product/list");

        // Verify the product exists in the list
        String productRowXpath = String.format("//tbody/tr[td[text()='%s']]", productName);
        assertTrue(driver.findElements(By.xpath(productRowXpath)).size() > 0,
                "The product should exist in the list before deletion");

        // Delete the product
        deleteProduct(driver, productName);

        // Verify the product no longer exists in the list
        assertEquals(0, driver.findElements(By.xpath(productRowXpath)).size(),
                "The product should no longer exist in the list after deletion");
    }

    private void createProduct(ChromeDriver driver, String productName, String quantity) {
        driver.get(baseUrl + "/product/create");
        driver.findElement(By.id("nameInput")).sendKeys(productName);
        driver.findElement(By.id("quantityInput")).sendKeys(quantity);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    private void deleteAllProducts(ChromeDriver driver) {
        driver.get(baseUrl + "/product/list");

        // Continuously check for and delete products until none remain.
        List<WebElement> deleteButtons = driver.findElements(By.cssSelector("a[href^='/product/delete/']"));
        while (!deleteButtons.isEmpty()) {
            try {
                // Click the first available delete button.
                deleteButtons.get(0).click();

                // Optionally wait here if your page needs time to update.
                // For example, you could use Thread.sleep(500) or an explicit wait.

                // Refresh the page and re-fetch the delete buttons.
                driver.get(baseUrl + "/product/list");
                deleteButtons = driver.findElements(By.cssSelector("a[href^='/product/delete/']"));
            } catch (StaleElementReferenceException e) {
                // If a stale element is encountered, re-fetch the delete buttons.
                deleteButtons = driver.findElements(By.cssSelector("a[href^='/product/delete/']"));
            }
        }
    }




    private void deleteProduct(ChromeDriver driver, String productName) {
        // Navigate to the product list page
        driver.get(baseUrl + "/product/list");

        // Find the delete button for the specific product and click it
        String deleteButtonXpath = String.format("//tbody/tr[td[text()='%s']]//a[contains(@href, '/product/delete/')]", productName);
        driver.findElement(By.xpath(deleteButtonXpath)).click();
    }
}