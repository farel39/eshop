package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentRepository {
    // In-memory storage for payments
    private Map<String, Payment> payments = new HashMap<>();
    // Mapping between Payment id and associated Order
    private Map<String, Order> paymentOrder = new HashMap<>();

    /**
     * Saves the given payment.
     * If a payment with the same id already exists, it is updated.
     * @param payment Payment to save.
     * @return The saved payment.
     */
    public Payment save(Payment payment) {
        return null;
    }

    /**
     * Finds a payment by its id.
     * @param id Payment id.
     * @return The Payment if found, or null otherwise.
     */
    public Payment findById(String id) {
        return null;
    }

    /**
     * Retrieves all payments.
     * @return A list of all Payment objects.
     */
    public List<Payment> findAll() {
        return null;
    }

    /**
     * Finds all payments matching the specified payment method.
     * @param method Payment method to filter by.
     * @return A list of Payment objects that match the given method.
     */
    public List<Payment> findAllByMethod(String method) {
        return null;
    }

    /**
     * Associates an Order with the given Payment.
     * @param payment Payment for which to assign an Order.
     * @param order Order to be assigned.
     */
    public void assignOrder(Payment payment, Order order) {

    }

    /**
     * Retrieves the Order associated with the given payment id.
     * @param paymentId The Payment id.
     * @return The associated Order, or null if none is assigned.
     */
    public Order findOrderByPaymentId(String paymentId) {

    }
}
