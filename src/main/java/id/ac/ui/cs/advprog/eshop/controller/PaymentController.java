package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    // Define a constant for the attribute key "payment"
    private static final String PAYMENT_ATTRIBUTE = "payment";

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Displays a form where a user can input a payment ID to lookup its details.
    @GetMapping("/detail")
    public String showPaymentDetailForm() {
        return "PaymentDetailForm";
    }

    // Displays the details of a specific payment.
    @GetMapping("/detail/{paymentId}")
    public String showPaymentDetail(@PathVariable("paymentId") String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute(PAYMENT_ATTRIBUTE, payment);
        return "PaymentDetail";
    }

    // Displays a list of all payments (admin view).
    @GetMapping("/admin/list")
    public String showAllPayments(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "PaymentAdminList";
    }

    // Displays details of a specific payment along with options to reject or accept it (admin view).
    @GetMapping("/admin/detail/{paymentId}")
    public String showPaymentAdminDetail(@PathVariable("paymentId") String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute(PAYMENT_ATTRIBUTE, payment);
        return "PaymentAdminDetail";
    }

    // Updates the status of a specific payment based on the provided status (admin view).
    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(@PathVariable("paymentId") String paymentId,
                                   @RequestParam("status") String status,
                                   Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        Payment updatedPayment = paymentService.setStatus(payment, status);
        model.addAttribute(PAYMENT_ATTRIBUTE, updatedPayment);
        return "PaymentAdminDetail";
    }
}
