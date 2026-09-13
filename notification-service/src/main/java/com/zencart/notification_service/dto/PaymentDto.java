package com.zencart.notification_service.dto;

import com.zencart.notification_service.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private Integer paymentId;
    private Boolean isPayed;
    private PaymentStatus paymentStatus;

    private Integer orderId;
    private Integer userId;
}
