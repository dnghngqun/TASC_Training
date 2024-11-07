package com.tasc.hongquan.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Material {
    @Id
    @Column(name = "material_id", nullable = false)
    private Integer id;

    @Column(name = "material_name")
    private String materialName;

}