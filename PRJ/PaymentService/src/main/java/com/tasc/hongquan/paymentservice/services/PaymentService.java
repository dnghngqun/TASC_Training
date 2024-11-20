package com.tasc.hongquan.paymentservice.services;

import com.tasc.hongquan.paymentservice.exception.MoMoException;
import com.tasc.hongquan.paymentservice.models.Payment;
import com.tasc.hongquan.paymentservice.models.momo.PaymentResponse;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface PaymentService {
    List<Payment> getAllPayments();
    PaymentResponse addPayment(Payment payment) throws Exception;
    void updatePaymentById(String id, Payment payment);
    void deletePaymentById(String id);
    ConcurrentHashMap<Integer, Integer> getMapOrderDetailIdAndQuantityByOrderId(int orderId);
    String executePayment(String orderId,String requestId,String orderInfo,String message,Integer resultCode);
}
