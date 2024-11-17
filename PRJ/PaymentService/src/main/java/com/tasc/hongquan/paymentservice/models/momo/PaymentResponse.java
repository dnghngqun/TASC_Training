package com.tasc.hongquan.paymentservice.models.momo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse extends Response{
    public PaymentResponse(Integer resultCode, String message){
        this.resultCode = resultCode;
        this.message = message;
    }

    private String requestId;
    private Long amount;
    private String payUrl;
    private String shortLink;
    private String deepLink;
    private String qrCodeUrl;
    private String deepLinkWebInApp;
}
