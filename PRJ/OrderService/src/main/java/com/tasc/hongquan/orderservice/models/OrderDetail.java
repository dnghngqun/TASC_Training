package com.tasc.hongquan.orderservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderDetail {
    private Integer id;
    private Integer orderId;
    private Integer quantity;
    private Integer productId;
    private BigDecimal price;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}