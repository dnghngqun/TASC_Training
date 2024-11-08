package com.tasc.hongquan.productservice.services;

import com.tasc.hongquan.productservice.dao.statement.ProductDAO;
import com.tasc.hongquan.productservice.dao.statement.ProductDAOImpl;
import com.tasc.hongquan.productservice.models.Product;
import com.tasc.hongquan.productservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductDAO productDAO;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductDAOImpl productDAO) {
        this.productRepository = productRepository;
        this.productDAO = productDAO;
    }

    @Override
    public int getCountProduct(Integer categoryId) {
        if (categoryId != null && categoryId > 0) {
            return productDAO.getCountProductWhereCategoryId(categoryId);
        }
        return productDAO.getCountProduct();
    }

    @Override
    public void addProduct(Product product) {
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Product productUpdate) {
        Product product = productRepository.findById(productUpdate.getId()).orElseThrow(() -> new RuntimeException("Product not found"));
        if (productUpdate.getName() != null) {
            product.setName(productUpdate.getName());
        }
        if (productUpdate.getPrice() != null) {
            product.setPrice(productUpdate.getPrice());
        }
        if (productUpdate.getDescription() != null) {
            product.setDescription(productUpdate.getDescription());
        }

        if (productUpdate.getImageUrl() != null) {
            product.setImageUrl(productUpdate.getImageUrl());
        }

        if (productUpdate.getStock() != null) {
            product.setStock(productUpdate.getStock());
        }

        product.setUpdatedAt(Instant.now());
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Page<Product> getAllProducts(int page, int size, Integer categoryId) {
        Pageable pageable = PageRequest.of(page, size);
        if (categoryId != null && categoryId > 0) {
            System.out.println("Category id: " + categoryId);
            return productRepository.getAllProductsByCategory(pageable, categoryId);
        }
        return productRepository.findAll(pageable);
    }
}
