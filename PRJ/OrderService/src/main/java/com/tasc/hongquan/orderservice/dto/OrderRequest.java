package com.tasc.hongquan.orderservice.dto;

import com.tasc.hongquan.orderservice.models.OrderDetail;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "UserId is required")
    private String userId;
    @NotEmpty(message = "Total price is required")
    private BigDecimal totalPrice;
    @NotEmpty(message = "Discount id is required")
    private Integer discountId;
    @NotEmpty(message = "Address id is required")
    private Integer addressId;

    private String note;
    @NotEmpty(message = "Order details is required")
    private List<OrderDetail> orderDetails;
    @NotEmpty(message = "Payment method is required")
    private String paymentMethod;
}
