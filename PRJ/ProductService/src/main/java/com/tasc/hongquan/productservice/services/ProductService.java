package com.tasc.hongquan.productservice.services;

import com.tasc.hongquan.productservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    void addProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(int id);

    Product getProductById(int id);

    Page<Product> getAllProducts(int page, int size);

    int getCountProduct();
}
