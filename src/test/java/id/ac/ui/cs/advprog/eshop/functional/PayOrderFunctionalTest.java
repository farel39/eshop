package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class PayOrderFunctionalTest extends BaseFunctionalTest {

    @Autowired
    private OrderService orderService;

    /**
     * Helper method to create a test order.
     * Since the Order constructor requires a non-empty product list, we create a dummy product.
     */
    private Order createTestOrder(String author) {
        Product product = new Product();
        product.setId("dummy-product-id");
        product.setName("Test Product");
        product.setQuantity(10);
        List<Product> products = new ArrayList<>();
        products.add(product);

        Order order = new Order(UUID.randomUUID().toString(), products, System.currentTimeMillis(), author);
        return orderService.createOrder(order);
    }

    /**
     * Test that the payment page (GET /order/pay/{orderId}) displays the order details correctly.
     */
    @Test
    void testShowPaymentPage(ChromeDriver driver) {
        Order order = createTestOrder("TestUser");
        driver.get(baseUrl + "/order/pay/" + order.getId());

        // Verify the page header.
        WebElement header = driver.findElement(By.tagName("h1"));
        assertEquals("Pay for Order", header.getText(), "Payment page header should be 'Pay for Order'");

        // Verify that the order details are displayed (e.g., the Order ID).
        WebElement orderIdElement = driver.findElement(By.xpath("//p[contains(text(),'Order ID:')]/span"));
        assertEquals(order.getId(), orderIdElement.getText(), "Displayed Order ID should match the created order");
    }

    /**
     * Test submitting the payment form using the Voucher Code payment method.
     */
    @Test
    void testPayOrderUsingVoucher(ChromeDriver driver) {
        Order order = createTestOrder("TestUserVoucher");
        driver.get(baseUrl + "/order/pay/" + order.getId());

        // The Voucher Code option is selected by default.
        // Fill in the voucher code field.
        WebElement voucherField = driver.findElement(By.id("voucherCode"));
        voucherField.sendKeys("VOUCHER123");

        // Submit the payment form.
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Verify the PaymentSuccess page is displayed.
        // For example, assume it displays a paragraph with "Payment ID:".
        WebElement paymentIdElement = driver.findElement(By.xpath("//*[contains(text(),'Payment ID:')]/span"));
        assertNotNull(paymentIdElement.getText(), "Payment ID should be displayed on the Payment Success page");
    }

    /**
     * Test submitting the payment form using the Bank Transfer payment method.
     */
    @Test
    void testPayOrderUsingBankTransfer(ChromeDriver driver) {
        Order order = createTestOrder("TestUserBank");
        driver.get(baseUrl + "/order/pay/" + order.getId());

        // Select the Bank Transfer payment method.
        WebElement bankRadio = driver.findElement(By.xpath("//input[@name='method' and @value='BANK_TRANSFER']"));
        bankRadio.click();

        // Fill in bank transfer fields.
        WebElement bankNameField = driver.findElement(By.id("bankName"));
        bankNameField.sendKeys("Test Bank");
        WebElement referenceCodeField = driver.findElement(By.id("referenceCode"));
        referenceCodeField.sendKeys("REF123456");

        // Submit the payment form.
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Verify the PaymentSuccess page is displayed.
        WebElement paymentIdElement = driver.findElement(By.xpath("//*[contains(text(),'Payment ID:')]/span"));
        assertNotNull(paymentIdElement.getText(), "Payment ID should be displayed on the Payment Success page");
    }
}
