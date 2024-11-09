package com.tasc.hongquan.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CartRequest {
    private int productId;
    private int quantity;
    private String userId;
}
