package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        // Generate a unique payment ID.
        String paymentId = UUID.randomUUID().toString();
        Payment payment = new Payment(paymentId, method, paymentData);
        Payment savedPayment = paymentRepository.save(payment);
        // Link the payment with its order.
        paymentRepository.assignOrder(savedPayment, order);
        return savedPayment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        if (payment == null) {
            throw new NoSuchElementException("Payment not found");
        }
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid payment status");
        }
        // Validate that the payment exists using getPayment().
        getPayment(payment.getId());

        // Update the status of the associated order if present.
        Order order = paymentRepository.findOrderByPaymentId(payment.getId());
        if (order != null) {
            updateOrderStatus(order, status);
        }

        Payment updatedPayment = new Payment(
                payment.getId(),
                payment.getMethod(),
                payment.getPaymentData(),
                status
        );
        return paymentRepository.save(updatedPayment);
    }

    private void updateOrderStatus(Order order, String status) {
        if (PaymentStatus.SUCCESS.getValue().equals(status)) {
            order.setStatus(OrderStatus.SUCCESS.getValue());
        } else {

            order.setStatus(OrderStatus.FAILED.getValue());
        }
    }

    @Override
    public Payment getPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) {
            throw new NoSuchElementException("Payment not found with id: " + paymentId);
        }
        return payment;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
