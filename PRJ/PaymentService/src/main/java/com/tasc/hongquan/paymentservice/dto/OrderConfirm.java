package com.tasc.hongquan.paymentservice.dto;

import com.tasc.hongquan.paymentservice.models.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderConfirm {
    private Order order;
    private String paymentMethod;
}
