package com.tasc.hongquan.paymentservice.task;

import com.tasc.hongquan.paymentservice.kafka.KafkaProducer;
import com.tasc.hongquan.paymentservice.models.EmailRetryQueue;
import com.tasc.hongquan.paymentservice.repositories.EmailRetryQueueRepository;
import com.tasc.hongquan.paymentservice.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class EmailRetrySchedule {
    private final EmailRetryQueueRepository emailRetryQueueRepository;
    private final EmailService emailService;
    private final KafkaAdmin kafkaAdmin;
    private final KafkaProducer kafkaProducer;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void retryEmail() {
        List<EmailRetryQueue> emailRetryQueues = emailRetryQueueRepository.findAll();
        for (EmailRetryQueue emailRetryQueue : emailRetryQueues) {
            try {
                if (isKafkaConnected()) {
                    kafkaProducer.sendRetryEmail(emailRetryQueue);
                } else {
                    emailService.sendRetryEmail(emailRetryQueue.getEmailTo(), emailRetryQueue.getSubject(), emailRetryQueue.getBody());
                }
                //delete email from db when success
            } catch (Exception e) {
                System.out.println("Error sending email to: " + emailRetryQueue.getEmailTo());
                //skip, wait retry again
                continue;
            }
            emailRetryQueueRepository.delete(emailRetryQueue);
        }
    }

    private boolean isKafkaConnected() {
        try {
            // Send simple message to check connection
            kafkaTemplate.send("ping-topic", "ping");
            return true;
        } catch (Exception e) {
            System.err.println("Kafka connection failed: " + e.getMessage());
            return false;
        }
    }
}
