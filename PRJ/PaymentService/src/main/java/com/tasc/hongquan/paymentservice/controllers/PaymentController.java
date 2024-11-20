package com.tasc.hongquan.paymentservice.controllers;

import com.tasc.hongquan.paymentservice.dto.ResponseObject;
import com.tasc.hongquan.paymentservice.models.Payment;
import com.tasc.hongquan.paymentservice.models.momo.PaymentResponse;
import com.tasc.hongquan.paymentservice.models.momo.RequestType;
import com.tasc.hongquan.paymentservice.services.OrderDetailService;
import com.tasc.hongquan.paymentservice.services.OrderDetailsServiceImpl;
import com.tasc.hongquan.paymentservice.services.PaymentService;
import com.tasc.hongquan.paymentservice.services.PaymentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderDetailService orderDetailService;
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final String secretKey;
    private final String accessKey;
    private final String partnerCode;
    private final String returnURL = "http://localhost:8088/api/v1/payments/momo/response";
    private final String notifyURL = "http://localhost:8088/api/v1/payments/notify";
    @Autowired
    public PaymentController(PaymentServiceImpl paymentService, OrderDetailsServiceImpl orderDetailService, @Value("${DEV_SECRET_KEY}") String secretKey,
                             @Value("${DEV_ACCESS_KEY}") String accessKey, @Value("${DEV_PARTNER_CODE}") String partnerCode) {
        this.paymentService = paymentService;
        this.orderDetailService = orderDetailService;
        this.secretKey = secretKey;
        this.accessKey = accessKey;
        this.partnerCode = partnerCode;

    }
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


    private String generateSignature(String orderId, Integer resultCode, BigDecimal amount, String secretKey) {
        String rawData = "orderId=" + orderId + "&resultCode=" + resultCode + "&amount=" + amount + "&key=" + secretKey;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating signature", e);
        }
    }

    @GetMapping("/momo/response")
    public String momoResponse(@RequestParam Map<String, String> params) {
        try {
            logger.info("Momo response: " + params);
            String parnerCode = params.get("partnerCode");
            String orderId = params.get("orderId");
            //requestid = payment id
            String requestId = params.get("requestId");
            BigDecimal amount = new BigDecimal(Double.parseDouble(params.get("amount")));
            String orderInfo = params.get("orderInfo");
            String orderType = params.get("orderType");
            String transId = params.get("transId");
            String message = params.get("message");
            String payType = params.get("payType");
            String responseTime = params.get("responseTime");
            String extraData = params.get("extraData");
            String signature = params.get("signature");
            Integer resultCode = Integer.valueOf(params.get("resultCode"));
            RequestType requestType = RequestType.CAPTURE_WALLET;

            logger.info("Secret key: " + secretKey);
            logger.info("Access key: " + accessKey);
            return paymentService.executePayment( orderId,  requestId,  orderInfo,  message,  resultCode);
        } catch (Exception e) {
            logger.error(e.getMessage());
//            return ResponseEntity.status(500).body("Momo failed");
            return "<html><body>" +
                    "<script>" +
                    "window.opener.postMessage(" +
                    "{ message: 'Momo failed' }, '*');" +
                    "window.close();" +  // Đóng popup sau khi gửi dữ liệu
                    "</script>" +
                    "</body></html>";
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updatePaymentById(@PathVariable String id, @RequestBody Payment paymentUpdate) {
        try {
            paymentService.updatePaymentById(id, paymentUpdate);
            return ResponseEntity.ok(new ResponseObject("ok", "Update payment successfully", null));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new ResponseObject("error", "Update payment failed", null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deletePaymentById(@PathVariable String id) {
        try {
            paymentService.deletePaymentById(id);
            return ResponseEntity.ok(new ResponseObject("ok", "Delete payment successfully", null));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new ResponseObject("error", "Delete payment failed", null));
        }
    }

}
