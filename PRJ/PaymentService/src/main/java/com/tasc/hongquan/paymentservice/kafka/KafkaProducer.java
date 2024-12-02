package com.tasc.hongquan.paymentservice.kafka;

import com.tasc.hongquan.paymentservice.dto.EmailRequest;
import com.tasc.hongquan.paymentservice.dto.OrderConfirm;
import com.tasc.hongquan.paymentservice.models.EmailRetryQueue;
import com.tasc.hongquan.paymentservice.models.Order;
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

    public void sendOrderConfirm(OrderConfirm orderConfirm) {
        kafkaTemplate.send("email-order-topic", orderConfirm);
        System.out.println("Email confirm queued: Order ID:" + orderConfirm.getOrder().getId());
    }

    public void sendRetryEmail(EmailRetryQueue emailRetryQueue) {
        kafkaTemplate.send("email-error-topic", emailRetryQueue);
        System.out.println("Email error retry queued: " + emailRetryQueue.getEmailTo());
    }
}
