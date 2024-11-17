package com.tasc.hongquan.paymentservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnerInfo {
    private String accessKey;
    private String partnerCode;
    private String secretKey;
    private String publicKey;


}
