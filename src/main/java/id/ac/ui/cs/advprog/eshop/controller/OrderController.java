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

    // GET /order/create
    // Displays the order creation form along with the list of available products.
    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "CreateOrder";
    }

    // POST /order/create
    // Processes the creation of a new order based on the selected products.
    @PostMapping("/create")
    public String createOrder(@RequestParam("author") String author,
                              @RequestParam(value = "productIds", required = false) List<String> productIds,
                              Model model) {
        if (productIds == null || productIds.isEmpty()) {
            model.addAttribute("error", "Please select at least one product.");
            // Repopulate the products for the view
            model.addAttribute("products", productService.findAll());
            return "CreateOrder";
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
            model.addAttribute("products", productService.findAll());
            return "CreateOrder";
        }

        // Create a new order with a generated id and the current timestamp.
        Order newOrder = new Order(UUID.randomUUID().toString(), selectedProducts, System.currentTimeMillis(), author);
        Order createdOrder = orderService.createOrder(newOrder);
        model.addAttribute("order", createdOrder);
        return "OrderCreated";
    }

    // GET /order/history
    @GetMapping("/history")
    public String showOrderHistoryForm() {
        return "OrderHistoryForm";
    }

    // POST /order/history
    @PostMapping("/history")
    public String showOrderHistory(@RequestParam("name") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "OrderHistory";
    }

    // GET /order/pay/{orderId}
    @GetMapping("/pay/{orderId}")
    public String showPaymentOrderPage(@PathVariable("orderId") String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "OrderPay";
    }

    // POST /order/pay/{orderId}
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
