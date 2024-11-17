package com.tasc.hongquan.paymentservice.services;

import com.tasc.hongquan.paymentservice.config.Environment;
import com.tasc.hongquan.paymentservice.exception.MoMoException;
import com.tasc.hongquan.paymentservice.models.Order;
import com.tasc.hongquan.paymentservice.models.Payment;
import com.tasc.hongquan.paymentservice.models.momo.PaymentResponse;
import com.tasc.hongquan.paymentservice.models.momo.RequestType;
import com.tasc.hongquan.paymentservice.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;
    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Transactional
    @Override
    public PaymentResponse addPayment(Payment payment) throws Exception {
        String requestId = String.valueOf(System.currentTimeMillis());
        String partnerClientId = "partnerClientId";
        String orderInfo = "Pay With Momo";
        String returnURL = "http:localhost:8088/api/v1/payments/success";
        String notifyURL = "https://google.com.vn";
        Environment environment = Environment.selectEnv("dev");
        CreateOrderMomo createOrderMomo = new CreateOrderMomo(environment);
        Integer orderId = payment.getOrder().getId();
        BigDecimal amount = payment.getOrder().getTotalPrice();
        PaymentResponse captureWalletMoMoResponse = createOrderMomo.process(environment, orderId.toString(), requestId,
                amount.toString(), orderInfo, returnURL,notifyURL, "", RequestType.PAY_WITH_ATM, Boolean.TRUE);

        payment.setCreatedAt(Instant.now());
        payment.setPaymentStatus("pending");
        paymentRepository.save(payment);
        return captureWalletMoMoResponse;
    }

    @Override
    public void updatePaymentById(int id, Payment paymentUpdate) {
        Payment payment = paymentRepository.findById(id).orElse(null);
        if(payment == null) {
            throw new RuntimeException("Payment not found");
        }
        if(paymentUpdate.getOrder() != null) {
            payment.setOrder(paymentUpdate.getOrder());
        }
        if(paymentUpdate.getPaymentMethod() != null) {
            payment.setPaymentMethod(paymentUpdate.getPaymentMethod());
        }
        if(paymentUpdate.getPaymentStatus() != null) {
            payment.setPaymentStatus(paymentUpdate.getPaymentStatus());
        }
        payment.setUpdatedAt(Instant.now());
        paymentRepository.save(payment);
    }

    @Override
    public void deletePaymentById(int id) {
        Payment payment = paymentRepository.findById(id).orElse(null);
        if(payment == null) {
            throw new RuntimeException("Payment not found");
        }
        paymentRepository.delete(payment);
    }
}
