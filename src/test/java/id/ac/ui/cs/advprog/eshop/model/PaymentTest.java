package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class PaymentTest {
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
    }

    // --- Voucher Code Sub-feature Tests ---

    @Test
    void testCreateVoucherPaymentValid() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678"); // Valid: 16 characters, correct prefix, at least 8 digits
        Payment payment = new Payment("payment-uuid-1", PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
        assertEquals(PaymentMethod.VOUCHER_CODE.getValue(), payment.getMethod());
    }

    @Test
    void testCreateVoucherPaymentInvalidLength() {
        paymentData.put("voucherCode", "ESHOP1234ABC567"); // 15 characters, invalid
        Payment payment = new Payment("payment-uuid-2", PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreateVoucherPaymentInvalidPrefix() {
        paymentData.put("voucherCode", "ASHOP1234ABC6784"); // Does not start with ESHOP
        Payment payment = new Payment("payment-uuid-3", PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreateVoucherPaymentInsufficientDigits() {
        paymentData.put("voucherCode", "ESHOPABCDEFGHXYZW"); // 16 characters but not enough digits
        Payment payment = new Payment("payment-uuid-4", PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreateVoucherPaymentNullVoucher() {
        paymentData.put("voucherCode", null); // 16 characters but not enough digits
        Payment payment = new Payment("payment-uuid-4", PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    // --- Bank Transfer Sub-feature Tests ---

    @Test
    void testCreateBankTransferPaymentValid() {
        paymentData.put("bankName", "Bank ABC");
        paymentData.put("referenceCode", "REF123456");
        Payment payment = new Payment("payment-uuid-5", PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
        assertEquals(PaymentMethod.BANK_TRANSFER.getValue(), payment.getMethod());
    }

    @Test
    void testCreateBankTransferPaymentEmptyBankName() {
        paymentData.put("bankName", " ");
        paymentData.put("referenceCode", "REF123456");
        Payment payment = new Payment("payment-uuid-6", PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreateBankTransferPaymentNullBankName() {
        paymentData.put("bankName", null);
        paymentData.put("referenceCode", "REF123456");
        Payment payment = new Payment("payment-uuid-6", PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreateBankTransferPaymentEmptyReferenceCode() {
        paymentData.put("bankName", "Bank ABC");
        paymentData.put("referenceCode", " ");
        Payment payment = new Payment("payment-uuid-7", PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreateBankTransferPaymentNullReferenceCode() {
        paymentData.put("bankName", "Bank ABC");
        paymentData.put("referenceCode", null);
        Payment payment = new Payment("payment-uuid-7", PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    // --- General Validation Tests ---

    @Test
    void testCreatePaymentWithNullPaymentData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-uuid-8", PaymentMethod.VOUCHER_CODE.getValue(), null);
        });
    }

    @Test
    void testCreatePaymentWithEmptyId() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("", PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        });
    }

    @Test
    void testCreatePaymentWithNullId() {
        paymentData.put("voucherCode", "ESHOP1235ABC5678");
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(null, PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        });
    }

    @Test
    void testCreatePaymentWithNullMethod() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-uuid-9", null, paymentData);
        });
    }

    @Test
    void testCreatePaymentWithEmptyMethod() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-uuid-9", "", paymentData);
        });
    }

    @Test
    void testCreatePaymentWithInvalidMethod() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-uuid-9", "INVALID_METHOD", paymentData);
        });
    }

}
