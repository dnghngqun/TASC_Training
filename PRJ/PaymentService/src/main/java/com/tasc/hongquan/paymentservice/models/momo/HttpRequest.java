package com.tasc.hongquan.paymentservice.models.momo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequest {
    private String method;
    private String endpoint;
    private String payload;
    private String contentType;
}
