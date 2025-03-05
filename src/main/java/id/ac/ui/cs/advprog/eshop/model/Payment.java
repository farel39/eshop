package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        // Validate required fields
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("method cannot be null or empty");
        }
        if (paymentData == null) {
            throw new IllegalArgumentException("paymentData cannot be null");
        }
        // Only allow specific payment methods
        if (!method.equals("VOUCHER_CODE") && !method.equals("BANK_TRANSFER")) {
            throw new IllegalArgumentException("Invalid payment method: " + method);
        }

        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        // Validate based on the payment method
        if (method.equals("VOUCHER_CODE")) {
            validateVoucherPayment();
        } else if (method.equals("BANK_TRANSFER")) {
            validateBankTransferPayment();
        }
    }

    private void validateVoucherPayment() {
        String voucherCode = paymentData.get("voucherCode");
        // Voucher must not be null, exactly 16 characters, start with "ESHOP", and contain at least 8 digits.
        if (voucherCode == null ||
                voucherCode.length() != 16 ||
                !voucherCode.startsWith("ESHOP") ||
                countDigits(voucherCode) < 8) {
            this.status = "REJECTED";
        } else {
            this.status = "SUCCESS";
        }
    }

    private void validateBankTransferPayment() {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        // Both bankName and referenceCode must be non-null and non-empty (after trimming)
        if (bankName == null || bankName.trim().isEmpty() ||
                referenceCode == null || referenceCode.trim().isEmpty()) {
            this.status = "REJECTED";
        } else {
            this.status = "SUCCESS";
        }
    }

    private int countDigits(String input) {
        int count = 0;
        for (char ch : input.toCharArray()) {
            if (Character.isDigit(ch)) {
                count++;
            }
        }
        return count;
    }


}
