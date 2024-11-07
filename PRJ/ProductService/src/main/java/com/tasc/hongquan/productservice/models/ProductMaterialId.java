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
public class ProductMaterialId implements java.io.Serializable {
    private static final long serialVersionUID = 3180570454145864042L;
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "material_id", nullable = false)
    private Integer materialId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductMaterialId entity = (ProductMaterialId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.materialId, entity.materialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, materialId);
    }

}