package com.tasc.hongquan.orderservice.services;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
