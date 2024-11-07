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
public class ProductCapacityId implements java.io.Serializable {
    private static final long serialVersionUID = -4473742901028215363L;
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "capacity_id", nullable = false)
    private Integer capacityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductCapacityId entity = (ProductCapacityId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.capacityId, entity.capacityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, capacityId);
    }

}