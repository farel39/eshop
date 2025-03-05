package id.ac.ui.cs.advprog.eshop.service;

import java.util.Map;
import java.util.List;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Order;

public interface PaymentService {
    Payment addPayment(Order order, String method, Map<String, String> paymentData);
    Payment setStatus(Payment payment, String status);
    Payment getPayment(String paymentId);
    List<Payment> getAllPayments();
}

