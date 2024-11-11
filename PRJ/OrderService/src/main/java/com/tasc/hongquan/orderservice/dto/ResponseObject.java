package com.tasc.hongquan.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseObject {
    private String status;
    private String message;
    private Object data;
}
