package com.tasc.hongquan.paymentservice.kafka;

import com.tasc.hongquan.paymentservice.dto.EmailRequest;
import com.tasc.hongquan.paymentservice.dto.OrderConfirm;
import com.tasc.hongquan.paymentservice.models.EmailRetryQueue;
import com.tasc.hongquan.paymentservice.repositories.EmailRetryQueueRepository;
import com.tasc.hongquan.paymentservice.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaConsumer {
    private final EmailService emailService;
    private final EmailRetryQueueRepository emailRetryQueueRepository;

    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void consumeEmail(EmailRequest emailRequest) {
        System.out.println("Processing email: " + emailRequest);
        emailService.sendEmail(emailRequest.getEmail(), emailRequest.getSubject(), emailRequest.getContent());
    }

    @KafkaListener(topics = "email-order-topic", groupId = "email-order-group", concurrency = "4")
    public void sendOrderConfirm(OrderConfirm orderConfirm) {
        System.out.println("Processing order confirm: " + orderConfirm);
        try {
            emailService.sendOrderConfirmation(orderConfirm.getOrder(), orderConfirm.getPaymentMethod());
        } catch (Exception e) {
            //TODO: save to db
            System.out.println("Error sending order confirmation email");
            String subject = "Xác nhận đơn hàng #" + orderConfirm.getOrder().getId();
            String emailContent = emailService.generateEmailContent(orderConfirm.getOrder(), "online");

            emailRetryQueueRepository.save(new EmailRetryQueue().builder()
                    .emailTo(orderConfirm.getOrder().getUser().getEmail())
                    .subject(subject)
                    .body(emailContent)
                    .build());
        }
    }

    @KafkaListener(topics = "email-error-topic", groupId = "email-error-group", concurrency = "4")
    public void sendRetryEmail(EmailRetryQueue emailRetryQueue) {
        System.out.println("Processing retry email: " + emailRetryQueue);
        try {
            emailService.sendRetryEmail(emailRetryQueue.getEmailTo(), emailRetryQueue.getSubject(), emailRetryQueue.getBody());
        } catch (Exception e) {
            System.out.println("Error sending order confirmation email");
            //save to db if error again
            emailRetryQueueRepository.save(emailRetryQueue);
        }
    }


}
