package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public class PaymentFunctionalTest extends BaseFunctionalTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    /**
     * Helper method to create a test order.
     * We create a dummy product since the Order constructor requires a non-empty product list.
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
     * Helper method to create a test payment using PaymentService.addPayment.
     * Now using "VOUCHER_CODE" with a valid voucher code.
     */
    private Payment createTestPayment(String author, double amount) {
        Order order = createTestOrder(author);
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("amount", String.valueOf(amount));
        paymentData.put("currency", "USD");
        // Provide a valid voucher code: 16 characters, starts with "ESHOP", and contains at least 8 digits.
        paymentData.put("voucherCode", "ESHOP12345678901");
        return paymentService.addPayment(order, "VOUCHER_CODE", paymentData);
    }

    /**
     * Test GET /payment/detail displays the payment detail form.
     */
    @Test
    public void testPaymentDetailForm(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/detail");
        // The page header should be "Payment Detail" according to the template.
        WebElement header = driver.findElement(By.tagName("h1"));
        assertEquals("Payment Detail Lookup", header.getText(), "Payment Detail form should be displayed");
    }

    /**
     * Test GET /payment/detail/[paymentId] displays payment details.
     */
    @Test
    public void testPaymentDetailById(ChromeDriver driver) {
        Payment payment = createTestPayment("TestUser", 100.0);

        driver.get(baseUrl + "/payment/detail/" + payment.getId());

        // Verify payment details are displayed.
        WebElement idElement = driver.findElement(By.xpath("//*[contains(text(),'Payment ID:')]/span"));
        assertEquals(payment.getId(), idElement.getText(), "Payment ID should match");

        // Instead of checking "Amount", we now check the "Method" field.
        WebElement methodElement = driver.findElement(By.xpath("//*[contains(text(),'Method:')]/span"));
        assertEquals("VOUCHER_CODE", methodElement.getText(), "Payment Method should match");

        WebElement statusElement = driver.findElement(By.xpath("//*[contains(text(),'Status:')]/span"));
        assertEquals(payment.getStatus(), statusElement.getText(), "Payment Status should match");
    }

    /**
     * Test GET /payment/admin/list displays all payments.
     */
    @Test
    public void testPaymentAdminList(ChromeDriver driver) {
        Payment payment = createTestPayment("AdminUser", 200.0);

        driver.get(baseUrl + "/payment/admin/list");

        // The admin list page header is "All Payments (Admin)".
        WebElement header = driver.findElement(By.tagName("h1"));
        assertEquals("All Payments (Admin)", header.getText(), "Payment Admin List header should be displayed");

        // Verify that the created payment appears in the list.
        WebElement paymentRow = driver.findElement(By.xpath("//*[contains(text(),'" + payment.getId() + "')]"));
        assertTrue(paymentRow.isDisplayed(), "The created payment should appear in the admin list");
    }

    /**
     * Test GET /payment/admin/detail/[paymentId] and POST /payment/admin/set-status/[paymentId]
     * for updating a payment status.
     */
    @Test
    public void testPaymentAdminDetailAndSetStatus(ChromeDriver driver) {
        // Create a test payment with an initial status.
        Payment payment = createTestPayment("AdminTest", 300.0);

        // Navigate to the admin detail page for the payment.
        driver.get(baseUrl + "/payment/admin/detail/" + payment.getId());

        // The page header should be "Payment Admin Detail" according to the template.
        WebElement header = driver.findElement(By.tagName("h1"));
        assertEquals("Payment Admin Detail", header.getText(), "Payment Admin Detail header should be displayed");

        // Locate the status dropdown and update button.
        Select statusSelect = new Select(driver.findElement(By.id("status")));
        // Select the "SUCCESS" option.
        statusSelect.selectByValue("SUCCESS");
        WebElement updateButton = driver.findElement(By.xpath("//button[text()='Update Status']"));
        updateButton.click();

        // After submission, assume the page reloads with the updated status.
        WebElement statusElement = driver.findElement(By.xpath("//*[contains(text(),'Status:')]/span"));
        assertEquals("SUCCESS", statusElement.getText(), "Payment status should be updated to SUCCESS");

        // Test rejection using another payment record.
        Payment payment2 = createTestPayment("AdminTestReject", 400.0);
        driver.get(baseUrl + "/payment/admin/detail/" + payment2.getId());
        Select statusSelect2 = new Select(driver.findElement(By.id("status")));
        // Select the "REJECTED" option.
        statusSelect2.selectByValue("REJECTED");
        WebElement updateButton2 = driver.findElement(By.xpath("//button[text()='Update Status']"));
        updateButton2.click();
        WebElement statusElement2 = driver.findElement(By.xpath("//*[contains(text(),'Status:')]/span"));
        assertEquals("REJECTED", statusElement2.getText(), "Payment status should be updated to REJECTED");
    }
}
