package com.tasc.hongquan.paymentservice.services;

import com.tasc.hongquan.paymentservice.models.Order;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmail(String to, String subject, String content);

    void sendOrderConfirmation(Order order, String paymentMethod) throws MessagingException;

    String generateEmailContent(Order order, String paymentMethod);

    void sendRetryEmail(String to, String subject, String content) throws MessagingException;
}
