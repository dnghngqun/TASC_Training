package com.tasc.hongquan.paymentservice.controllers;

import com.tasc.hongquan.paymentservice.client.OrderClient;
import com.tasc.hongquan.paymentservice.client.ProductClient;
import com.tasc.hongquan.paymentservice.constant.Parameter;
import com.tasc.hongquan.paymentservice.dto.MomoNotifyResponse;
import com.tasc.hongquan.paymentservice.dto.ResponseObject;
import com.tasc.hongquan.paymentservice.models.Order;
import com.tasc.hongquan.paymentservice.models.Payment;
import com.tasc.hongquan.paymentservice.models.momo.PaymentResponse;
import com.tasc.hongquan.paymentservice.models.momo.RequestType;
import com.tasc.hongquan.paymentservice.services.OrderDetailService;
import com.tasc.hongquan.paymentservice.services.OrderDetailsServiceImpl;
import com.tasc.hongquan.paymentservice.services.PaymentService;
import com.tasc.hongquan.paymentservice.services.PaymentServiceImpl;
import com.tasc.hongquan.paymentservice.util.Encoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private OrderClient orderClient;
    private ProductClient productClient;
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
            // 1. Kiểm tra chữ ký (signature)
//            String requestRawData = new StringBuilder()
//                    .append("accessKey=").append(accessKey).append("&")
//                    .append("amount=").append(amount).append("&")
//                    .append("extraData=").append(extraData).append("&")
//                    .append("ipnUrl=").append(notifyURL).append("&")
//                    .append("orderId=").append(orderId).append("&")
//                    .append("orderInfo=").append(orderInfo).append("&")
//                    .append("partnerCode=").append(partnerCode).append("&")
//                    .append("redirectUrl=").append(returnURL).append("&")
//                    .append("requestId=").append(requestId).append("&")
//                    .append("requestType=").append("captureWallet")
//                    .toString();
//
//            logger.info("Signature: " + signature);
//            logger.info("SecretKey: " + secretKey);
//            String expectedSignature = Encoder.signHmacSHA256(requestRawData, secretKey);
//            logger.info("Expected signature: " + expectedSignature);
//            if (!expectedSignature.equals(signature)) {
//                logger.error("Invalid signature");
//                return "<html><body>" +
//                        "<script>" +
//                        "window.opener.postMessage(" +
//                        "{ message: 'Invalid signature' }, '*');" +
//                        "window.close();" +  // Đóng popup sau khi gửi dữ liệu
//                        "</script>" +
//                        "</body></html>";
////                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
//            }

            // 2. Xử lý kết quả giao dịch
            if (resultCode == 0) {
                // Giao dịch thành công
                logger.info("Order " + orderId + " has been paid successfully.");
                //TODO: Update status payment
                paymentService.updatePaymentById(requestId, new Payment().builder().paymentStatus("success").build());
                //TODO: UPDATE STOCK PRODUCT
                ConcurrentHashMap<Integer, Integer> productStock = paymentService.getMapOrderDetailIdAndQuantityByOrderId(Integer.parseInt(orderId));
                ResponseEntity<ResponseObject> updateStock = productClient.updateStockProduct(productStock);
                if(updateStock.getStatusCode().is2xxSuccessful()) {
                    logger.info("Update stock successfully");
                    //Todo: Update database success for orderDetails
                    List<Integer> orderDetailSuccess = (List<Integer>) updateStock.getBody().getData();
                    for(Integer orderDetailId: productStock.keySet()){
                        if(orderDetailSuccess.contains(orderDetailId)) {
                            //update status success for orderDetail
                            orderDetailService.updateOrderDetailStatus(orderDetailId, "success");
                        }else {
                            //update status error for orderDetail
                            orderDetailService.updateOrderDetailStatus(orderDetailId, "error");
                        }
                    }
                }else {
                    logger.error("Update stock error");
                }
                // TODO: Update database for orderId
                ResponseEntity<ResponseObject> orderResponse = orderClient.updateOrderById(Integer.parseInt(orderId), new Order().builder().status("success").build());
                if(orderResponse.getStatusCode() == HttpStatus.OK) {
                    logger.info("Update order " + orderId + " successfully");
                } else {
                    logger.error("Update order " + orderId + " failed");
                }
            } else {
                // Failed transaction
                //TODO: Update Payment to error
                paymentService.updatePaymentById(requestId, new Payment().builder().paymentStatus("error").build());
                System.out.println("Payment for Order " + orderId + " failed: " + message);
            }

            return "<html><body>" +
                    "<script>" +
                    "window.opener.postMessage(" +
                    "{ message: '" + message + "', orderId: '" + orderId + "', orderInfo: '" + orderInfo + "' }, '*');" +
                    "window.close();" +  // Đóng popup sau khi gửi dữ liệu
                    "</script>" +
                    "</body></html>";
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
