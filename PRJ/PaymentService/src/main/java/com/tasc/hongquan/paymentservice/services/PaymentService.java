package com.tasc.hongquan.paymentservice.services;

import com.tasc.hongquan.paymentservice.exception.MoMoException;
import com.tasc.hongquan.paymentservice.models.Payment;
import com.tasc.hongquan.paymentservice.models.momo.PaymentResponse;

import java.util.List;

public interface PaymentService {
    List<Payment> getAllPayments();
    PaymentResponse addPayment(Payment payment) throws Exception;
    void updatePaymentById(int id, Payment payment);
    void deletePaymentById(int id);


}
