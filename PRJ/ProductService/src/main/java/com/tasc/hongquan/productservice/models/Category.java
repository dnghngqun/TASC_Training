package com.tasc.hongquan.productservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

}