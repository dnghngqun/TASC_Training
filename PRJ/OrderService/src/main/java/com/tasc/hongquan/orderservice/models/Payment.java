package com.tasc.hongquan.orderservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private Order order;

    private String paymentMethod;

    private String paymentStatus;

    private Instant paidAt;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;

}