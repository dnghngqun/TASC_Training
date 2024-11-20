package com.tasc.hongquan.productservice.repositories;

import com.tasc.hongquan.productservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> getAllProductsByCategory(Pageable pageable, int categoryId);

    @Query(value = "SELECT * FROM order_details od JOIN products p ON od.product_id = p.product_id WHERE od.order_detail_id = :orderDetailId", nativeQuery = true)
    Optional<Product> findByOrderDetailId(@Param("orderDetailId") int orderDetailId);
}
