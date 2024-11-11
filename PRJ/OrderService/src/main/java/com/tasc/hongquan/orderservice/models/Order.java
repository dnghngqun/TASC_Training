package com.tasc.hongquan.orderservice.models;

import lombok.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private String status;
    private Integer discountId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}