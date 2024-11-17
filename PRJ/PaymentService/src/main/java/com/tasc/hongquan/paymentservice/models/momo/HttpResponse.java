package com.tasc.hongquan.paymentservice.models.momo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.Headers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {
    private int status;
    private String data;
    private Headers headers;
}
