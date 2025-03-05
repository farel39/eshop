package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    private final String id;
    private final String method;
    private final String status;
    private final Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        validateInput(id, method, paymentData);
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        // Evaluate the payment status using the evaluator which now accepts a String
        PaymentStatus evaluatedStatus = PaymentStatusEvaluator.evaluate(method, paymentData);
        this.status = evaluatedStatus.getValue();
    }

    private void validateInput(String id, String method, Map<String, String> paymentData) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Payment id cannot be empty");
        }
        if (method == null || method.isEmpty() || !PaymentMethod.contains(method)) {
            throw new IllegalArgumentException("Invalid payment method");
        }
        if (paymentData == null) {
            throw new IllegalArgumentException("Payment data cannot be null");
        }
    }


}
