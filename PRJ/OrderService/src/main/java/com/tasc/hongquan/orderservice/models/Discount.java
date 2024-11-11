package com.tasc.hongquan.orderservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Discount {
    private Integer id;
    private BigDecimal amount;
    private Instant expires;
    private String code;
    private Integer quantity;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}