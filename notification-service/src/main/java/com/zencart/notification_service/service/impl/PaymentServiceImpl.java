package com.zencart.notification_service.service.impl;

import com.zencart.notification_service.dto.PaymentDto;
import com.zencart.notification_service.entity.Payment;
import com.zencart.notification_service.mapper.PaymentMapper;
import com.zencart.notification_service.repo.PaymentRepo;
import com.zencart.notification_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {


    private final PaymentRepo paymentRepo;

    @Override
    public Payment savePayment(PaymentDto paymentDto) {
        try {
            return paymentRepo.save(PaymentMapper.map(paymentDto));
        } catch (Exception e) {
            log.error("Error saving payment: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Payment getPayment(Integer paymentId) {
        return paymentRepo.findById(paymentId).orElse(null);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    @Override
    public void deletePayment(Integer paymentId) {
        paymentRepo.deleteById(paymentId);
    }
}
