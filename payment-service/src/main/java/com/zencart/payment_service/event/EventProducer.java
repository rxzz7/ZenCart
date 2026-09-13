package com.zencart.payment_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class EventProducer {

    private final KafkaTemplate<String, String > kafkaTemplate;

    public void send(String topic, String message){
        //result contains information about the successful Kafka send
        //ex contains the exception if sending failed
        kafkaTemplate.send(topic, message).whenComplete((result, ex)->{
            if (ex != null){
                log.error("Failed to send Kafka message to topic {}: {}", topic, ex.getMessage());
            }else {
                log.debug("Sent message to topic {} offset {}", topic, result.getRecordMetadata().offset());
            }
        });
    }
}
