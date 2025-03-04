package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;

import java.util.List;

public interface OrderService {

    public Order createOrder(Order order);  // Creates a new order

    public Order updateStatus(String orderId, String status);  // Updates the status of an order

    public Order findById(String orderId);  // Retrieves an order by its ID

    public List<Order> findAllByAuthor(String author);  // Finds all orders by the author's name
}

