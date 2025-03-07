package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    // Define constants to avoid duplicating literal strings.
    private static final String ATTRIBUTE_PRODUCTS = "products";
    private static final String VIEW_CREATE_ORDER = "CreateOrder";

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ProductService productService;

    @Autowired
    public OrderController(OrderService orderService,
                           PaymentService paymentService,
                           ProductService productService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.productService = productService;
    }


    // Displays the order creation form along with the list of available products.
    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute(ATTRIBUTE_PRODUCTS, products);
        return VIEW_CREATE_ORDER;
    }


    // Processes the creation of a new order based on the selected products.
    @PostMapping("/create")
    public String createOrder(@RequestParam("author") String author,
                              @RequestParam(value = "productIds", required = false) List<String> productIds,
                              Model model) {
        if (productIds == null || productIds.isEmpty()) {
            model.addAttribute("error", "Please select at least one product.");
            // Repopulate the products for the view
            model.addAttribute(ATTRIBUTE_PRODUCTS, productService.findAll());
            return VIEW_CREATE_ORDER;
        }

        List<Product> selectedProducts = new ArrayList<>();
        for (String productId : productIds) {
            Product product = productService.findById(productId);
            if (product != null) {
                selectedProducts.add(product);
            }
        }

        if (selectedProducts.isEmpty()) {
            model.addAttribute("error", "Selected products are invalid.");
            model.addAttribute(ATTRIBUTE_PRODUCTS, productService.findAll());
            return VIEW_CREATE_ORDER;
        }

        // Create a new order with a generated id and the current timestamp.
        Order newOrder = new Order(UUID.randomUUID().toString(), selectedProducts, System.currentTimeMillis(), author);
        Order createdOrder = orderService.createOrder(newOrder);
        model.addAttribute("order", createdOrder);
        return "OrderCreated";
    }


    @GetMapping("/history")
    public String showOrderHistoryForm() {
        return "OrderHistoryForm";
    }


    @PostMapping("/history")
    public String showOrderHistory(@RequestParam("name") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "OrderHistory";
    }


    @GetMapping("/pay/{orderId}")
    public String showPaymentOrderPage(@PathVariable("orderId") String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "OrderPay";
    }


    @PostMapping("/pay/{orderId}")
    public String payOrder(@PathVariable("orderId") String orderId,
                           @RequestParam("method") String method,
                           @RequestParam Map<String, String> paymentData,
                           Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            return "Error";
        }
        Payment payment = paymentService.addPayment(order, method, paymentData);
        model.addAttribute("paymentId", payment.getId());
        return "PaymentSuccess";
    }
}
