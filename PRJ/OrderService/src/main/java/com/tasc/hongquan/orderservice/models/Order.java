package com.tasc.hongquan.orderservice.models;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer id;
    private String userId;
    private BigDecimal totalPrice;
    private String status; //'create','pending', 'delivered', 'success', 'cancel', 'error'
    private Integer addressId;
    private Integer discountId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", addressId=" + addressId +
                ", discountId=" + discountId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}

