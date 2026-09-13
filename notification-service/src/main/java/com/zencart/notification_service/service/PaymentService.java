package com.zencart.notification_service.service;

import com.zencart.notification_service.dto.PaymentDto;
import com.zencart.notification_service.entity.Payment;

import java.util.List;

public interface PaymentService {
    Payment savePayment(PaymentDto paymentDto);
    Payment getPayment(Integer paymentId);
    List<Payment> getAllPayments();
    void deletePayment(Integer paymentId);
}
