package com.zencart.notification_service.mapper;

import com.zencart.notification_service.dto.PaymentDto;
import com.zencart.notification_service.entity.Payment;

public interface PaymentMapper {

        static Payment map(PaymentDto paymentDto){
            if(paymentDto == null) return null;
            return Payment.builder()
                    .paymentId(paymentDto.getPaymentId())
                    .paymentStatus(paymentDto.getPaymentStatus())
                    .isPayed(paymentDto.getIsPayed())
                    .userId(paymentDto.getUserId())
                    .orderId(paymentDto.getOrderId())
                    .build();
        }

        static PaymentDto map(Payment payment){
            if(payment == null) return null;
            return PaymentDto.builder()
                    .paymentId(payment.getPaymentId())
                    .paymentStatus(payment.getPaymentStatus())
                    .isPayed(payment.getIsPayed())
                    .userId(payment.getUserId())
                    .orderId(payment.getOrderId())
                    .build();
        }
}
