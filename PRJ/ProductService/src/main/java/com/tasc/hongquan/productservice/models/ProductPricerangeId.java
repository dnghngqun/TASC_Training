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
public class ProductPricerangeId implements java.io.Serializable {
    private static final long serialVersionUID = 1605182393916062936L;
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "price_range_id", nullable = false)
    private Integer priceRangeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductPricerangeId entity = (ProductPricerangeId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.priceRangeId, entity.priceRangeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, priceRangeId);
    }

}