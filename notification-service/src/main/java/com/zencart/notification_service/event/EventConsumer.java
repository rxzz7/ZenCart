package com.zencart.notification_service.event;

import com.google.gson.Gson;
import com.zencart.notification_service.constant.KafkaConstant;
import com.zencart.notification_service.dto.EmailDetails;
import com.zencart.notification_service.dto.PaymentDto;
import com.zencart.notification_service.entity.PaymentStatus;
import com.zencart.notification_service.service.EmailService;
import com.zencart.notification_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class EventConsumer {

    private final EmailService emailService;
    private final PaymentService paymentService;
    private final Gson gson = new Gson();

    @KafkaListener(topics = KafkaConstant.STATUS_PAYMENT_SUCCESSFUL,
            groupId = "${notification.kafka.consumer-group-id}")
    public void paymentOrderKafkaOnboarding(String message){
        log.info("Payment event received on notification-service");
        PaymentDto paymentDto = gson.fromJson(message, PaymentDto.class);
        paymentService.savePayment(paymentDto);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient("sarjusk1586@gmail.com")///
                .msgBody(msgBody(paymentDto))
                .subject("Payment successfully , Order with UserId: " + paymentDto.getUserId())
                .attachment("Please, check the full information in invoice: " + LocalDateTime.now())
                .build();
        String emailResult = emailService.sendSimpleMail(emailDetails);
    }

    private String msgBody(PaymentDto paymentDto) {
        return "Payment for order id: " + paymentDto.getOrderId() +
                " \n  is payed : " + paymentDto.getIsPayed() +
                "\n Payment Status is: " + paymentDto.getPaymentStatus() +
                "\n\nDate: " + LocalDate.now() +
                "\nTime: " + LocalTime.now();
    }
}
