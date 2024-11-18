package com.tasc.hongquan.paymentservice.controllers;

import com.tasc.hongquan.paymentservice.dto.MomoNotifyResponse;
import com.tasc.hongquan.paymentservice.dto.ResponseObject;
import com.tasc.hongquan.paymentservice.models.Order;
import com.tasc.hongquan.paymentservice.models.Payment;
import com.tasc.hongquan.paymentservice.models.momo.PaymentResponse;
import com.tasc.hongquan.paymentservice.services.PaymentService;
import com.tasc.hongquan.paymentservice.util.Encoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);


    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllPayments() {
        try {
            return ResponseEntity.ok(new ResponseObject("ok", "Get all payments successfully", paymentService.getAllPayments()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new ResponseObject("error", "Get all payments failed", null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addPayment(@RequestBody Payment payment) {
        try {
            PaymentResponse paymentResponse = paymentService.addPayment(payment);
            String payUrl = paymentResponse.getPayUrl();
            return ResponseEntity.ok(new ResponseObject("ok", "Add payment successfully", payUrl));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new ResponseObject("error", "Add payment failed", null));
        }
    }
    @PostMapping("/notify")
    public ResponseEntity<?> handleNotify(@RequestBody MomoNotifyResponse notifyResponse) {
        logger.info("Received notify from MoMo: {}", notifyResponse);
        try {
            logger.info("Received notify from MoMo: {}", notifyResponse);
//            try {
//                // 1. Kiểm tra chữ ký
//                String rawData = "partnerCode=" + notifyResponse.getPartnerCode() +
//                        "&orderId=" + notifyResponse.getOrderId() +
//                        "&requestId=" + notifyResponse.getRequestId() +
//                        "&amount=" + notifyResponse.getAmount() +
//                        "&orderInfo=" + notifyResponse.getOrderInfo() +
//                        "&orderType=" + notifyResponse.getOrderType() +
//                        "&transId=" + notifyResponse.getTransId() +
//                        "&errorCode=" + notifyResponse.getErrorCode() +
//                        "&message=" + notifyResponse.getMessage() +
//                        "&localMessage=" + notifyResponse.getLocalMessage() +
//                        "&responseTime=" + notifyResponse.getResponseTime();
//
//                String secretKey = "your_secret_key"; // Lấy từ tài khoản MoMo
//                String calculatedSignature = Encoder.hashSHA256(rawData);
//
//                if (!calculatedSignature.equals(notifyResponse.getSignature())) {
//                    throw new IllegalArgumentException("Invalid signature from MoMo");
//                }
//
//                // 2. Cập nhật trạng thái giao dịch
//                if ("0".equals(notifyResponse.getErrorCode())) {
//                    // Giao dịch thành công
////                    paymentService.updatePaymentStatus(notifyResponse.getOrderId(), "success");
//                } else {
//                    // Giao dịch thất bại
////                    paymentService.updatePaymentStatus(notifyResponse.getOrderId(), "failed");
//                }
//            paymentService.updatePaymentStatus(notifyResponse);
            return ResponseEntity.ok("Notify received successfully");
        } catch (Exception e) {
            logger.error("Error processing notify: " + e.getMessage(), e);
            return ResponseEntity.status(500).body("Error processing notify");
        }
    }

    @GetMapping("/momo/response")
    public ResponseEntity<String> momoResponse(@RequestParam Map<String, String> params) {
        try {
            logger.info("Momo response: " + params);
            return ResponseEntity.ok("Momo Response");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body("Momo failed");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updatePaymentById(@PathVariable int id, @RequestBody Payment paymentUpdate) {
        try {
            paymentService.updatePaymentById(id, paymentUpdate);
            return ResponseEntity.ok(new ResponseObject("ok", "Update payment successfully", null));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new ResponseObject("error", "Update payment failed", null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deletePaymentById(@PathVariable int id) {
        try {
            paymentService.deletePaymentById(id);
            return ResponseEntity.ok(new ResponseObject("ok", "Delete payment successfully", null));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new ResponseObject("error", "Delete payment failed", null));
        }
    }







}
