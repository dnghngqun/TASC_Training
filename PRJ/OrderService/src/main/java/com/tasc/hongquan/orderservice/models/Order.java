package com.tasc.hongquan.orderservice.models;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
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
}

