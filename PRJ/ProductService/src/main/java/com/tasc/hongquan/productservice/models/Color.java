package com.tasc.hongquan.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Color {
    @Id
    @Column(name = "color_id", nullable = false)
    private Integer id;

    @Column(name = "color_name")
    private String colorName;

}