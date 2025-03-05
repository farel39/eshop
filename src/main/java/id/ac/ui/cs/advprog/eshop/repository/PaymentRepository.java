package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.*;
import java.util.stream.Collectors;

public class PaymentRepository {
    // In-memory storage for payments
    private final Map<String, Payment> payments = new HashMap<>();
    private final Map<String, Order> paymentOrder = new HashMap<>();

    /**
     * Saves or updates a payment.
     *
     * @param payment Payment to save.
     * @return The saved payment.
     */
    public Payment save(Payment payment) {
        payments.put(payment.getId(), payment);
        return payment;
    }

    /**
     * Finds a payment by its ID.
     *
     * @param id Payment ID.
     * @return The Payment if found, otherwise null.
     */
    public Payment findById(String id) {
        return payments.get(id);
    }

    /**
     * Retrieves all payments.
     *
     * @return A list of all Payment objects.
     */
    public List<Payment> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(payments.values()));
    }

    /**
     * Finds all payments matching a specific payment method.
     *
     * @param method Payment method to filter by.
     * @return A list of Payment objects that match the given method.
     */
    public List<Payment> findAllByMethod(String method) {
        return payments.values().stream()
                .filter(payment -> payment.getMethod().equals(method))
                .collect(Collectors.toList());
    }

    /**
     * Associates an Order with a Payment.
     *
     * @param payment Payment to link.
     * @param order Order to be assigned.
     */
    public void assignOrder(Payment payment, Order order) {
        paymentOrder.put(payment.getId(), order);
    }

    /**
     * Retrieves the Order associated with a given payment ID.
     *
     * @param paymentId The Payment ID.
     * @return The associated Order, or null if not found.
     */
    public Order findOrderByPaymentId(String paymentId) {
        return paymentOrder.get(paymentId);
    }
}
