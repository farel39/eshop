package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

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
        Payment existing = paymentRepository.findById(payment.getId());
        if (existing == null) {
            throw new NoSuchElementException("Payment not found");
        }
        Order order = paymentRepository.findOrderByPaymentId(payment.getId());
        if (order != null) {
            if (PaymentStatus.SUCCESS.getValue().equals(status)) {
                order.setStatus(PaymentStatus.SUCCESS.getValue());
            } else {
                order.setStatus("FAILED");
            }
        }
        Payment updatedPayment = new Payment(payment.getId(), payment.getMethod(), payment.getPaymentData(), status);
        return paymentRepository.save(updatedPayment);
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
