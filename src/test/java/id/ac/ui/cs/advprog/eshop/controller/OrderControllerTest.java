package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(OrderControllerTest.TestConfig.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ProductService productService; // Added to support OrderController dependency

    @TestConfiguration
    static class TestConfig {

        @Bean
        public OrderService orderService() {
            return Mockito.mock(OrderService.class);
        }

        @Bean
        public PaymentService paymentService() {
            return Mockito.mock(PaymentService.class);
        }

        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }

    // Helper method to create a dummy product.
    private Product createDummyProduct() {
        Product product = new Product();
        product.setId("eb558e9f-1c39-4608-8860-71af6af63bd6");
        product.setName("Sampo Cap Bambang");
        product.setQuantity(2);
        return product;
    }

    @Test
    public void testShowCreateOrderForm() throws Exception {
        Mockito.when(productService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateOrder"));
    }

    @Test
    public void testShowOrderHistoryForm() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistoryForm"));
    }

    @Test
    public void testShowOrderHistory() throws Exception {
        String authorName = "John Doe";
        Product dummyProduct = createDummyProduct();
        Order order = new Order("order1", Collections.singletonList(dummyProduct),
                System.currentTimeMillis(), authorName);
        Mockito.when(orderService.findAllByAuthor(authorName))
                .thenReturn(Collections.singletonList(order));

        mockMvc.perform(post("/order/history")
                        .param("name", authorName))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(view().name("OrderHistory"));
    }

    @Test
    public void testShowPaymentOrderPage() throws Exception {
        String orderId = "12345";
        Product dummyProduct = createDummyProduct();
        Order order = new Order(orderId, Collections.singletonList(dummyProduct),
                System.currentTimeMillis(), "anyAuthor");
        Mockito.when(orderService.findById(orderId)).thenReturn(order);

        mockMvc.perform(get("/order/pay/" + orderId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("order", order))
                .andExpect(view().name("OrderPay"));
    }

    @Test
    public void testPayOrder() throws Exception {
        String orderId = "12345";
        Product dummyProduct = createDummyProduct();
        Order order = new Order(orderId, Collections.singletonList(dummyProduct),
                System.currentTimeMillis(), "anyAuthor");
        Mockito.when(orderService.findById(orderId)).thenReturn(order);

        Payment payment = new Payment("payment123", "VOUCHER_CODE", Collections.emptyMap());
        Mockito.when(paymentService.addPayment(Mockito.eq(order), Mockito.anyString(), Mockito.anyMap()))
                .thenReturn(payment);

        mockMvc.perform(post("/order/pay/" + orderId)
                        .param("method", "VOUCHER_CODE")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("paymentId", payment.getId()))
                .andExpect(view().name("PaymentSuccess"));
    }

    @Test
    public void testPayOrderNotFound() throws Exception {
        String orderId = "nonExistentOrder";
        Mockito.when(orderService.findById(orderId)).thenReturn(null);

        mockMvc.perform(post("/order/pay/" + orderId)
                        .param("method", "VOUCHER_CODE")
                        .param("voucherCode", "NON_EXISTENT"))
                .andExpect(status().isOk())
                .andExpect(view().name("Error"));
    }

    // --- New tests for createOrder endpoint ---

    @Test
    public void testCreateOrderWithoutProducts() throws Exception {
        // When no productIds parameter is provided, expect an error.
        Mockito.when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/order/create")
                        .param("author", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Please select at least one product."))
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("CreateOrder"));
    }

    @Test
    public void testCreateOrderWithEmptyProductIds() throws Exception {
        Mockito.when(productService.findAll()).thenReturn(Collections.emptyList());

        // Pass an empty array to simulate productIds being provided but empty.
        mockMvc.perform(post("/order/create")
                        .param("author", "John Doe")
                        .param("productIds", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Please select at least one product."))
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("CreateOrder"));
    }


    @Test
    public void testCreateOrderWithInvalidProducts() throws Exception {
        // When productIds are provided but none match a valid product.
        String invalidProductId = "invalid-id";
        Mockito.when(productService.findById(invalidProductId)).thenReturn(null);
        Mockito.when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/order/create")
                        .param("author", "John Doe")
                        .param("productIds", invalidProductId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Selected products are invalid."))
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("CreateOrder"));
    }

    @Test
    public void testCreateOrderWithValidProducts() throws Exception {
        // When valid productIds are provided, create the order.
        String validProductId = "valid-id";
        Product dummyProduct = createDummyProduct();
        dummyProduct.setId(validProductId); // Ensure ID matches the request
        Mockito.when(productService.findById(validProductId)).thenReturn(dummyProduct);

        // Stub order creation: match any Order instance and return a dummy order.
        Order dummyOrder = new Order("order-123", Collections.singletonList(dummyProduct),
                System.currentTimeMillis(), "John Doe");
        Mockito.when(orderService.createOrder(Mockito.any(Order.class))).thenReturn(dummyOrder);

        mockMvc.perform(post("/order/create")
                        .param("author", "John Doe")
                        .param("productIds", validProductId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(view().name("OrderCreated"));
    }
}
