package com.zencart.payment_service.mapper;

import com.zencart.payment_service.dto.OrderDto;
import com.zencart.payment_service.dto.PaymentDto;
import com.zencart.payment_service.dto.UserDto;
import com.zencart.payment_service.entity.Payment;

public interface PaymentMapper {

    static PaymentDto map(final Payment payment){
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .isPayed(payment.getIsPayed())
                .paymentStatus(payment.getPaymentStatus())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .orderDto(
                        OrderDto.builder()
                                .orderId(payment.getOrderId())
                                .build())
                .userDto(
                        UserDto.builder()
                        .userId(payment.getUserId())
                        .build())
                .build();

    }
    static Payment map(final PaymentDto paymentDto){
        return Payment.builder()
                .paymentId(paymentDto.getPaymentId())
                .orderId(paymentDto.getOrderId())
                .userId(paymentDto.getUserId())
                .isPayed(paymentDto.getIsPayed())
                .paymentStatus(paymentDto.getPaymentStatus())
                .build();
    }
}
