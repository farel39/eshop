package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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

    @ParameterizedTest(name = "{index} => voucherCode={0}")
    @NullSource
    @ValueSource(strings = { "ESHOP1234ABC567",  // Invalid length (15 characters)
            "ASHOP1234ABC6784", // Invalid prefix (does not start with ESHOP)
            "ESHOPABCDEFGHXYZW" // Insufficient digits
    })
    void testCreateVoucherPaymentInvalidVoucher(String voucherCode) {
        paymentData.put("voucherCode", voucherCode);
        Payment payment = new Payment("payment-uuid-2", PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
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

    @ParameterizedTest
    @MethodSource("provideInvalidBankTransferPaymentData")
    void testCreateBankTransferPaymentInvalidData(String bankName, String referenceCode) {
        paymentData.put("bankName", bankName);
        paymentData.put("referenceCode", referenceCode);
        Payment payment = new Payment("payment-uuid", PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    private static Stream<Arguments> provideInvalidBankTransferPaymentData() {
        return Stream.of(
                Arguments.of(" ", "REF123456"),
                Arguments.of(null, "REF123456"),
                Arguments.of("Bank ABC", " "),
                Arguments.of("Bank ABC", null)
        );
    }

    // --- General Validation Tests ---

    @Test
    void testCreatePaymentWithNullPaymentData() {
        // Pre-compute any values that might throw or clutter the lambda.
        String voucherCodeValue = PaymentMethod.VOUCHER_CODE.getValue();

        // Now the lambda contains only the constructor invocation.
        assertThrows(IllegalArgumentException.class, () ->
                new Payment("payment-uuid-8", voucherCodeValue, null)
        );
    }



    @Test
    void testCreatePaymentWithEmptyId() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        String voucherCodeValue = PaymentMethod.VOUCHER_CODE.getValue();
        assertThrows(IllegalArgumentException.class, () ->
                new Payment("", voucherCodeValue, paymentData)
        );
    }


    @Test
    void testCreatePaymentWithNullId() {
        paymentData.put("voucherCode", "ESHOP1235ABC5678");
        String voucherCodeValue = PaymentMethod.VOUCHER_CODE.getValue();
        assertThrows(IllegalArgumentException.class, () ->
                new Payment(null, voucherCodeValue, paymentData)
        );
    }



    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "INVALID_METHOD"})
    void testCreatePaymentWithInvalidMethod(String paymentMethod) {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-uuid-9", paymentMethod, paymentData);
        });
    }


}
