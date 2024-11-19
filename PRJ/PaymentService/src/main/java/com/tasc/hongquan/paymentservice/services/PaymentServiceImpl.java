package com.tasc.hongquan.paymentservice.services;

import com.tasc.hongquan.paymentservice.config.Environment;
import com.tasc.hongquan.paymentservice.exception.MoMoException;
import com.tasc.hongquan.paymentservice.models.Order;
import com.tasc.hongquan.paymentservice.models.Payment;
import com.tasc.hongquan.paymentservice.models.momo.PaymentResponse;
import com.tasc.hongquan.paymentservice.models.momo.RequestType;
import com.tasc.hongquan.paymentservice.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
@AllArgsConstructor
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;
    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
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
        String returnURL = "http://localhost:8088/api/v1/payments/momo/response";
        String notifyURL = "http://localhost:8088/api/v1/payments/notify";
        Environment environment = Environment.selectEnv("dev");
        String orderId = payment.getOrder().getId().toString();
        BigDecimal totalPrice = payment.getOrder().getTotalPrice();
        logger.info("Amount: " + totalPrice);
        BigDecimal amount = new BigDecimal(Double.parseDouble(totalPrice.toString()));
        PaymentResponse captureWalletMoMoResponse = CreateOrderMomo.process(environment, orderId, requestId, amount,
                orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);

        payment.setCreatedAt(Instant.now());
        payment.setPaymentStatus("pending");
        logger.info("Payment: " + payment.toString());
//        paymentRepository.save(payment);
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
