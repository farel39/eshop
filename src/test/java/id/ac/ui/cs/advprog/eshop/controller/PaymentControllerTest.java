package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment paymentDetail;

    private Payment acceptedPayment;
    private Product dummyProduct;


    @BeforeEach
    void setUp() {
        // Initialize MockMvc using standalone setup.
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        // Payment used for detail views (with id "1")
        paymentDetail = new Payment("1", "VOUCHER_CODE", Map.of("voucherCode", "DUMMY_CODE"));


        // Payment returned after status update (e.g., accepted)
        acceptedPayment = new Payment("payment123", "VOUCHER_CODE", Map.of("voucherCode", "ESHOP1234ABC5678"));

        // Create dummy product using setters
        dummyProduct = new Product();
        dummyProduct.setId("eb558e9f-1c39-4608-8860-71af6af63bd6");
        dummyProduct.setName("Sampo Cap Bambang");
        dummyProduct.setQuantity(2);

    }

    @Test
    void testShowPaymentDetailForm() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetailForm"));
    }

    @Test
    void testShowPaymentDetail() throws Exception {
        Mockito.when(paymentService.getPayment("1")).thenReturn(paymentDetail);

        mockMvc.perform(get("/payment/detail/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetail"))
                .andExpect(model().attribute("payment", paymentDetail));
    }

    @Test
    void testShowAllPayments() throws Exception {
        // Create a second payment for variety.
        Payment anotherPayment = new Payment("2", "VOUCHER_CODE", Map.of("voucherCode", "DUMMY_CODE2"));
        List<Payment> payments = Arrays.asList(paymentDetail, anotherPayment);

        Mockito.when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminList"))
                .andExpect(model().attribute("payments", payments));
    }

    @Test
    void testShowPaymentAdminDetail() throws Exception {
        Mockito.when(paymentService.getPayment("1")).thenReturn(paymentDetail);

        mockMvc.perform(get("/payment/admin/detail/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminDetail"))
                .andExpect(model().attribute("payment", paymentDetail));
    }

    @Test
    void testSetPaymentStatus() throws Exception {
        Mockito.when(paymentService.getPayment("1")).thenReturn(paymentDetail);
        Mockito.when(paymentService.setStatus(paymentDetail, "accepted")).thenReturn(acceptedPayment);

        mockMvc.perform(post("/payment/admin/set-status/1")
                        .param("status", "accepted"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminDetail"))
                .andExpect(model().attribute("payment", acceptedPayment));
    }


}
