package com.tasc.hongquan.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Capacity {
    @Id
    @Column(name = "capacity_id", nullable = false)
    private Integer id;

    @Column(name = "size", precision = 5, scale = 2)
    private BigDecimal size;

}