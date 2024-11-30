package com.tasc.hongquan.paymentservice.kafka;

import com.tasc.hongquan.paymentservice.dto.EmailRequest;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEmail(String topic, EmailRequest emailRequest) {
        kafkaTemplate.send(topic, emailRequest);
        System.out.println("Email queued: " + emailRequest);
    }
}
