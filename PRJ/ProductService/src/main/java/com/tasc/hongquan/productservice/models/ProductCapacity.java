package com.tasc.hongquan.productservice.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Product_Capacity")
public class ProductCapacity {
    @EmbeddedId
    private ProductCapacityId id;

}