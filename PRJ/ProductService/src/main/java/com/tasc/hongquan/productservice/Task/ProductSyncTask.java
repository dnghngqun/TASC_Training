package com.tasc.hongquan.productservice.Task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasc.hongquan.productservice.dao.statement.ProductDAO;
import com.tasc.hongquan.productservice.models.Category;
import com.tasc.hongquan.productservice.models.Product;
import com.tasc.hongquan.productservice.repositories.ProductRepository;
import com.tasc.hongquan.productservice.services.CategoryService;
import com.tasc.hongquan.productservice.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class ProductSyncTask {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final CategoryService categoryService;
    public static final String PRODUCT_ALL_PREFIX = "product_all:";
    @Qualifier("redisTemplate")
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;


    @Scheduled(fixedRate = 1000 * 60 * 11) // 11 minutes
    public void syncProductAllFromDbToRedis() {
        int size = 12;
        int countProduct = productService.getCountProduct(0) | 0;
        int totalPages = (int) Math.ceil((double) countProduct / size);
        for (int i = 0; i < totalPages; i++) {
            final int page = i;
            String productAllKey = PRODUCT_ALL_PREFIX + page + size;
            executorService.submit(() -> {
                Pageable pageable = PageRequest.of(page, size);
                Page<Product> products = productRepository.findAll(pageable);
                double longExpires = 10 * 60 + Math.ceil(Math.random() * 10);
                try {
                    String jsonData = objectMapper.writeValueAsString(products);
                    redisTemplate.opsForValue().set(productAllKey, jsonData, (long) longExpires, TimeUnit.SECONDS);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 13)
    public void syncProductAllByCategory() {
        try {
            int size = 12;
            List<Category> categories = categoryService.getAllCategories();
            for (Category category : categories) {
                int page = 0;
                double longExpires = 10 * 60 + Math.ceil(Math.random() * 10);
                executorService.submit(() -> {
                    Pageable pageable = PageRequest.of(page, size);
                    String productKey = PRODUCT_ALL_PREFIX + page + "+" + size + category.getId();
                    Page<Product> products = productRepository.getAllProductsByCategory(pageable, category.getId());
                    String jsonData = null;
                    try {
                        jsonData = objectMapper.writeValueAsString(products);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    redisTemplate.opsForValue().set(productKey, jsonData, (long) longExpires, TimeUnit.SECONDS);

                });
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 15) // 15 minutes
    public void syncProductHomePageFromDbToRedis() {
        int countProduct = productService.getCountProduct(0) | 0;
        final int page = 0;
        final int size = 10;
        String productAllKey = PRODUCT_ALL_PREFIX + page + "+" + size;
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageable);
        double longExpires = 10 * 60 + Math.ceil(Math.random() * 10);
        String jsonData = null;
        try {
            jsonData = objectMapper.writeValueAsString(products);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(productAllKey, jsonData, (long) longExpires, TimeUnit.SECONDS);
    }

}
