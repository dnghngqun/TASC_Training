package com.tasc.hongquan.productservice.repositories;

import com.tasc.hongquan.productservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> getAllProductsByCategory(Pageable pageable, int categoryId);
}
