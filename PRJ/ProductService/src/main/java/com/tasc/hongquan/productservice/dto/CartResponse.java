package com.tasc.hongquan.productservice.dto;

import com.tasc.hongquan.productservice.models.Cart;
import com.tasc.hongquan.productservice.models.Product;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartResponse {
    private Product product;
    private int quantity;
}
