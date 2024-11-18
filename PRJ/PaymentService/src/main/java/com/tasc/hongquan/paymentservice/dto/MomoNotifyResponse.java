package com.tasc.hongquan.paymentservice.dto;

import lombok.Data;

@Data
public class MomoNotifyResponse {
    private String partnerCode;
    private String orderId;
    private String requestId;
    private String amount;
    private String orderInfo;
    private String orderType;
    private String transId;
    private String errorCode;
    private String message;
    private String localMessage;
    private String responseTime;
    private String signature;
}
