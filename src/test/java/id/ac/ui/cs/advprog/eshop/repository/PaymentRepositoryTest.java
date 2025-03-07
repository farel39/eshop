package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Payment payment1;
    Payment payment2;
    Payment payment3;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        // Payment 1: Valid voucher code (should be SUCCESS)
        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        payment1 = new Payment("payment-uuid-1", PaymentMethod.VOUCHER_CODE.getValue(), paymentData1);

        // Payment 2: Valid bank transfer (should be SUCCESS)
        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("bankName", "Bank ABC");
        paymentData2.put("referenceCode", "REF123456");
        payment2 = new Payment("payment-uuid-2", PaymentMethod.BANK_TRANSFER.getValue(), paymentData2);

        // Payment 3: Invalid voucher code (should be REJECTED)
        Map<String, String> paymentData3 = new HashMap<>();
        paymentData3.put("voucherCode", "ESHOP1234ABC567"); // Only 15 characters
        payment3 = new Payment("payment-uuid-3", PaymentMethod.VOUCHER_CODE.getValue(), paymentData3);
    }

    // --- Payment Repository Tests ---

    @Test
    void testSaveCreate() {
        Payment result = paymentRepository.save(payment1);
        Payment findResult = paymentRepository.findById(payment1.getId());

        assertEquals(payment1.getId(), result.getId());
        assertEquals(payment1.getId(), findResult.getId());
        assertEquals(payment1.getMethod(), findResult.getMethod());
        assertEquals(payment1.getStatus(), findResult.getStatus());
    }

    @Test
    void testSaveUpdate() {
        // Save original payment1
        paymentRepository.save(payment1);

        // Create an updated version of payment1 (simulate update with an invalid voucher)
        Map<String, String> newPaymentData = new HashMap<>();
        newPaymentData.put("voucherCode", "ESHOPINVALIDCODE"); // This is invalid, so status should be REJECTED
        Payment updatedPayment = new Payment(payment1.getId(), PaymentMethod.VOUCHER_CODE.getValue(), newPaymentData);
        Payment result = paymentRepository.save(updatedPayment);

        Payment findResult = paymentRepository.findById(payment1.getId());
        assertEquals(payment1.getId(), result.getId());
        assertEquals(PaymentStatus.REJECTED.getValue(), findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdFound() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);

        Payment findResult = paymentRepository.findById(payment2.getId());
        assertNotNull(findResult);
        assertEquals(payment2.getId(), findResult.getId());
        assertEquals(payment2.getMethod(), findResult.getMethod());
        assertEquals(payment2.getStatus(), findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        Payment findResult = paymentRepository.findById("non-existent-id");
        assertNull(findResult);
    }

    @Test
    void testFindAll() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(3, allPayments.size());
    }

    @Test
    void testFindAllByMethod() {
        paymentRepository.save(payment1); // VOUCHER_CODE, SUCCESS
        paymentRepository.save(payment2); // BANK_TRANSFER, SUCCESS
        paymentRepository.save(payment3); // VOUCHER_CODE, REJECTED

        // Expect two payments with VOUCHER_CODE and one with BANK_TRANSFER
        List<Payment> voucherPayments = paymentRepository.findAllByMethod(PaymentMethod.VOUCHER_CODE.getValue());
        List<Payment> bankPayments = paymentRepository.findAllByMethod(PaymentMethod.BANK_TRANSFER.getValue());

        assertEquals(2, voucherPayments.size());
        assertEquals(1, bankPayments.size());
    }

    // --- Payment-Order Mapping Tests ---

    @Test
    void testAssignOrderToPayment() {
        // Create a sample Order (simulate as in OrderRepositoryTest)
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId("product-uuid-1");
        product.setName("Test Product");
        product.setQuantity(1);
        products.add(product);

        Order order = new Order("order-uuid-1", products, 1708560000L, "Test Author");

        paymentRepository.save(payment1);
        paymentRepository.assignOrder(payment1, order);

        Order mappedOrder = paymentRepository.findOrderByPaymentId(payment1.getId());
        assertNotNull(mappedOrder);
        assertEquals("order-uuid-1", mappedOrder.getId());
    }

    @Test
    void testFindOrderByPaymentIdIfNoOrderAssigned() {
        paymentRepository.save(payment1);
        Order mappedOrder = paymentRepository.findOrderByPaymentId(payment1.getId());
        assertNull(mappedOrder);
    }

    @Test
    void testUpdateAssignedOrder() {
        // Create initial and updated Order instances
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId("product-uuid-1");
        product.setName("Test Product");
        product.setQuantity(1);
        products.add(product);

        Order order1 = new Order("order-uuid-1", products, 1708560000L, "Test Author");
        Order order2 = new Order("order-uuid-2", products, 1708570000L, "Test Author 2");

        paymentRepository.save(payment1);
        paymentRepository.assignOrder(payment1, order1);
        Order mappedOrder = paymentRepository.findOrderByPaymentId(payment1.getId());
        assertEquals("order-uuid-1", mappedOrder.getId());

        // Update the mapping with a new Order
        paymentRepository.assignOrder(payment1, order2);
        Order updatedMappedOrder = paymentRepository.findOrderByPaymentId(payment1.getId());
        assertEquals("order-uuid-2", updatedMappedOrder.getId());
    }
}
