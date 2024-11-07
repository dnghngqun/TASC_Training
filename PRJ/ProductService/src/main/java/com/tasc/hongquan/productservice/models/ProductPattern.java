package com.tasc.hongquan.productservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Product_Pattern")
public class ProductPattern {
    @EmbeddedId
    private ProductPatternId id;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @MapsId("patternId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pattern_id", nullable = false)
    private Pattern pattern;

}