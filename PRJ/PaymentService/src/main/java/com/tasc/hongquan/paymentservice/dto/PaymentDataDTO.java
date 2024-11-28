package com.tasc.hongquan.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDataDTO {
    private String orderId;
    private String paymentId;
}
