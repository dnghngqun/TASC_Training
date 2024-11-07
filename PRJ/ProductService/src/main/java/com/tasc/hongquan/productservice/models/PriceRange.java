package com.tasc.hongquan.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PriceRange {
    @Id
    @Column(name = "price_range_id", nullable = false)
    private Integer id;

    @Column(name = "range_description")
    private String rangeDescription;

}