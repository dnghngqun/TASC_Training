package com.tasc.hongquan.orderservice.dto;

import com.tasc.hongquan.orderservice.models.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String userId;
    private BigDecimal totalPrice;
    private Integer discountId;
    private List<OrderDetail> orderDetails;
}
