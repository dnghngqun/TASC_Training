package com.hongquan.example.repositories;

import com.hongquan.example.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean deleteById(int id);
    @Query("SELECT p.name, p.price, c.name FROM Product p JOIN p.category c")
    List<Object[]> findProductsAndCategories();
}
