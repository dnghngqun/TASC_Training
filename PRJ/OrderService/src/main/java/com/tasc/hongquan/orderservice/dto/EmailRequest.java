package com.tasc.hongquan.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    //người nhận
    private String recipient;
    private String subject;
    private String content;
}
