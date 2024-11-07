package com.tasc.hongquan.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Pattern {
    @Id
    @Column(name = "pattern_id", nullable = false)
    private Integer id;

    @Column(name = "pattern_name")
    private String patternName;

}