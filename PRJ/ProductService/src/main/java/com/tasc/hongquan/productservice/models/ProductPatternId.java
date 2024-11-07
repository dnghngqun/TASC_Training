package com.tasc.hongquan.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ProductPatternId implements java.io.Serializable {
    private static final long serialVersionUID = 2249284650063821052L;
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "pattern_id", nullable = false)
    private Integer patternId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductPatternId entity = (ProductPatternId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.patternId, entity.patternId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, patternId);
    }

}