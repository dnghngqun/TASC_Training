package com.tasc.hongquan.paymentservice.services;


import com.tasc.hongquan.paymentservice.client.OrderClient;
import com.tasc.hongquan.paymentservice.client.ProductClient;
import com.tasc.hongquan.paymentservice.config.Environment;
import com.tasc.hongquan.paymentservice.dto.PaymentDataDTO;
import com.tasc.hongquan.paymentservice.dto.ResponseObject;
import com.tasc.hongquan.paymentservice.exception.MoMoException;
import com.tasc.hongquan.paymentservice.models.Order;
import com.tasc.hongquan.paymentservice.models.Payment;
import com.tasc.hongquan.paymentservice.models.momo.PaymentResponse;
import com.tasc.hongquan.paymentservice.models.momo.QueryStatusTransactionResponse;
import com.tasc.hongquan.paymentservice.models.momo.RequestType;
import com.tasc.hongquan.paymentservice.processor.QueryTransactionStatus;
import com.tasc.hongquan.paymentservice.repositories.OrderDetailRepository;
import com.tasc.hongquan.paymentservice.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;
    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final OrderDetailRepository orderDetailRepository;
    private OrderClient orderClient;
    private ProductClient productClient;
    private final OrderDetailService orderDetailService;
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
        payment.setId(requestId);
        payment.setCreatedAt(Instant.now());
        payment.setPaymentStatus("pending");
        logger.info("Payment: " + payment.toString());
        logger.info("Save payment to database");
        paymentRepository.save(payment);
        return captureWalletMoMoResponse;
    }

    @Override
    public QueryStatusTransactionResponse checkStatusPayment(String orderId, String requestId) {
        try {
            Environment environment = Environment.selectEnv("dev");
            QueryStatusTransactionResponse queryStatusTransactionResponse = QueryTransactionStatus.process(environment, orderId, requestId);
            logger.info("Query status response: " + queryStatusTransactionResponse);
            return queryStatusTransactionResponse;
        }catch (Exception e) {
            log.error("Error when check status payment: " + e.getMessage());
        }
    }

    @Override
    public List<PaymentDataDTO> checkTransaction() {
        return paymentRepository.finalAllTransactionPendingAndTimeLongerThan5Minutes();
    }

    @Override
    public void updatePaymentById(String id, Payment paymentUpdate) {
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
    public void deletePaymentById(String id) {
        Payment payment = paymentRepository.findById(id).orElse(null);
        if(payment == null) {
            throw new RuntimeException("Payment not found");
        }
        paymentRepository.delete(payment);
    }

    @Override
    public ConcurrentHashMap<Integer, Integer> getMapOrderDetailIdAndQuantityByOrderId(int orderId) {
        List<Object[]> listOrderDetailAndQuantity =  orderDetailRepository.getMapOrderDetailIdAndQuantityByOrderId(orderId);
        ConcurrentHashMap<Integer, Integer> mapOrderDetailIdAndQuantity = new ConcurrentHashMap<>();
        //convert to concurentHashMap
        for(Object[] row: listOrderDetailAndQuantity){
            int orderDetailId = (int) row[0];
            int quantity = (int) row[1];
            mapOrderDetailIdAndQuantity.put(orderDetailId, quantity);

        }
        return mapOrderDetailIdAndQuantity;
    }

    @Override
    public String executePayment(String orderId, String requestId, String orderInfo, String message, Integer resultCode) {
        String paymentStatus = "error";
        String userId = paymentRepository.getUserIdByPaymentId(requestId);
        logger.debug("User id: " + userId);
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
            paymentStatus = "success";
            //TODO: Update status payment
            updatePaymentById(requestId, new Payment().builder().paymentStatus("success").build());
            //TODO: DELETE CART
            ResponseEntity<ResponseObject> deleteCart = productClient.clearCart(userId);
            //TODO: UPDATE STOCK PRODUCT
            ConcurrentHashMap<Integer, Integer> productStock = getMapOrderDetailIdAndQuantityByOrderId(Integer.parseInt(orderId));
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
            updatePaymentById(requestId, new Payment().builder().paymentStatus("error").build());
            logger.error("Payment for Order " + orderId + " failed: " + message);
        }

        return "<html><body>" +
                "<script>" +
                "window.opener.postMessage(" +
                "{ message: '" + message + "', orderId: '" + orderId + "', orderInfo: '" + orderInfo + "' , paymentStatus: '" + paymentStatus + "'}, '*');" +
                "window.close();" +  // Đóng popup sau khi gửi dữ liệu
                "</script>" +
                "</body></html>";
    }
}
