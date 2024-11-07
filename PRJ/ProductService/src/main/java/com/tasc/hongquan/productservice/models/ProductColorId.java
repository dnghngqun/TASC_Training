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
public class ProductColorId implements java.io.Serializable {
    private static final long serialVersionUID = 5571248941255314684L;
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "color_id", nullable = false)
    private Integer colorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductColorId entity = (ProductColorId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.colorId, entity.colorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, colorId);
    }

}