package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;
    Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setId("eb558e9f-1c39-4608-8860-71af6af63bd6");
        product1.setName("Sampo Cap Bambang");
        product1.setQuantity(2);
        products.add(product1);
        order = new Order("order-123", products, 1708560001L, "John Doe");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment("payment-123", "VOUCHER_CODE", paymentData);
    }

    @Test
    void testAddPaymentWithValidVoucherCode() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentWithInvalidVoucherCode() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "INVALIDCODE");

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertNotNull(result);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusToSuccess() {
        when(paymentRepository.findById("payment-123")).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusToRejected() {
        when(paymentRepository.findById("payment-123")).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertNotNull(result);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusWhenPaymentNotFound() {
        // Simulate scenario where repository returns null for the payment lookup
        when(paymentRepository.findById(payment.getId())).thenReturn(null);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue()));
        assertEquals("Payment not found", exception.getMessage());
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    void testSetStatusWithNullPayment() {
        // Test the branch when the passed Payment is null.
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> paymentService.setStatus(null, PaymentStatus.SUCCESS.getValue()));
        assertEquals("Payment not found", exception.getMessage());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testSetStatusToSuccessUpdatesOrder() {
        // Associate an order with the payment.
        when(paymentRepository.findById("payment-123")).thenReturn(payment);
        when(paymentRepository.findOrderByPaymentId("payment-123")).thenReturn(order);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        // The order status should be updated to SUCCESS.
        assertEquals(PaymentStatus.SUCCESS.getValue(), order.getStatus());
        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusToRejectedUpdatesOrder() {
        // Associate an order with the payment.
        when(paymentRepository.findById("payment-123")).thenReturn(payment);
        when(paymentRepository.findOrderByPaymentId("payment-123")).thenReturn(order);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        // The order status should be updated to "FAILED".
        assertEquals("FAILED", order.getStatus());
        assertNotNull(result);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusWithInvalidStatus() {
        // When an invalid status is provided, no stubbing is necessary because the exception is thrown immediately.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> paymentService.setStatus(payment, "INVALID_STATUS"));
        assertEquals("Invalid payment status", exception.getMessage());
        verify(paymentRepository, never()).save(any(Payment.class));
    }


    @Test
    void testGetPaymentIfExists() {
        when(paymentRepository.findById("payment-123")).thenReturn(payment);

        Payment result = paymentService.getPayment("payment-123");
        assertNotNull(result);
        assertEquals("payment-123", result.getId());
    }

    @Test
    void testGetPaymentIfNotExists() {
        when(paymentRepository.findById("nonexistent")).thenReturn(null);

        assertThrows(NoSuchElementException.class,
                () -> paymentService.getPayment("nonexistent"));
    }

    @Test
    void testGetAllPayments() {
        when(paymentRepository.findAll()).thenReturn(List.of(payment));

        var result = paymentService.getAllPayments();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
