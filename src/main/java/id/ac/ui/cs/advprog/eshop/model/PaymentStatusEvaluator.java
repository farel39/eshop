package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import java.util.Map;

public class PaymentStatusEvaluator {
    private PaymentStatusEvaluator() {
        // Private constructor to prevent instantiation
    }

    public static PaymentStatus evaluate(String method, Map<String, String> paymentData) {
        return switch (method) {
            case "VOUCHER_CODE" -> evaluateVoucher(paymentData.get("voucherCode"));
            case "BANK_TRANSFER" -> evaluateBankTransfer(paymentData.get("bankName"), paymentData.get("referenceCode"));
            default -> PaymentStatus.REJECTED;
        };
    }

    private static PaymentStatus evaluateVoucher(String voucher) {
        if (voucher != null
                && voucher.length() == 16
                && voucher.startsWith("ESHOP")
                && countDigits(voucher) >= 8) {
            return PaymentStatus.SUCCESS;
        }
        return PaymentStatus.REJECTED;
    }

    private static PaymentStatus evaluateBankTransfer(String bankName, String referenceCode) {
        if (bankName == null || bankName.trim().isEmpty()
                || referenceCode == null || referenceCode.trim().isEmpty()) {
            return PaymentStatus.REJECTED;
        }
        return PaymentStatus.SUCCESS;
    }

    private static int countDigits(String input) {
        int count = 0;
        for (char ch : input.toCharArray()) {
            if (Character.isDigit(ch)) {
                count++;
            }
        }
        return count;
    }
}