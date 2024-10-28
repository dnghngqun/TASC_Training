package com.hongquan.example.services;


import com.hongquan.example.models.Product;
import com.hongquan.example.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public boolean deleteProduct(int id) {
        return productRepository.deleteById(id);
    }
    public List<Map<String, Object>> getProductWithCategory() {
        List<Object[]> results = productRepository.findProductsAndCategories();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("productName", row[0]);
            map.put("productPrice", row[1]);
            map.put("categoryName", row[2]);
            response.add(map);
        }

        return response;
    }
}
