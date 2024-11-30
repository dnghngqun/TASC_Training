package com.tasc.hongquan.orderservice.kafka;

import com.tasc.hongquan.orderservice.dto.EmailRequest;
import com.tasc.hongquan.orderservice.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void consumeEmail(EmailRequest emailRequest) {
        System.out.println("Processing email: " + emailRequest);
        emailService.sendEmail(emailRequest.getRecipient(), emailRequest.getSubject(), emailRequest.getContent());
    }
}
