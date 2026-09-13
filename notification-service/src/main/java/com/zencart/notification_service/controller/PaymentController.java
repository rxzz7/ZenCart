package com.zencart.notification_service.controller;

import com.zencart.notification_service.dto.PaymentDto;
import com.zencart.notification_service.entity.Payment;
import com.zencart.notification_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment-notifications")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> savePayment(@RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(paymentService.savePayment(paymentDto));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPayment(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(Integer.parseInt(String.format(paymentId).strip())));
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable String paymentId) {
        paymentService.deletePayment(Integer.parseInt(String.format(paymentId).strip()));
        return ResponseEntity.noContent().build();
    }
}
