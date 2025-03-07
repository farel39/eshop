package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class PaymentStatusEvaluatorTest {
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
    }


    @Test
    void testEvaluateInvalidPaymentMethod() {

        // Act: call evaluate with an unsupported method
        PaymentStatus result = PaymentStatusEvaluator.evaluate("CREDIT_CARD", paymentData);

        // Assert: verify that the default branch returns REJECTED
        assertEquals(PaymentStatus.REJECTED, result, "Expected REJECTED for an unsupported payment method");
    }
}
