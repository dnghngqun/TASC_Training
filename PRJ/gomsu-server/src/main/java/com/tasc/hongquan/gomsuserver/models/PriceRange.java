package com.tasc.hongquan.gomsuserver.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PriceRange {
    @Id
    @Column(name = "price_range_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "range_description")
    private String rangeDescription;

}