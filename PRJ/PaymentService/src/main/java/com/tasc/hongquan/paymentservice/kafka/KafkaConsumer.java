package com.tasc.hongquan.paymentservice.kafka;

import com.tasc.hongquan.paymentservice.dto.EmailRequest;
import com.tasc.hongquan.paymentservice.services.EmailService;
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
        emailService.sendEmail(emailRequest.getEmail(), emailRequest.getSubject(), emailRequest.getContent());
    }
}
