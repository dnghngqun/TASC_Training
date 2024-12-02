package com.tasc.hongquan.productservice.Task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasc.hongquan.productservice.dto.ProductRelated;
import com.tasc.hongquan.productservice.kafka.KafkaProducer;
import com.tasc.hongquan.productservice.models.Product;
import com.tasc.hongquan.productservice.repositories.ProductRepository;
import com.tasc.hongquan.productservice.services.CategoryService;
import com.tasc.hongquan.productservice.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


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
    private KafkaProducer kafkaProducer;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS) // 1 day
    public void saveProductToCache() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            executorService.submit(() -> {
                kafkaProducer.sendProduct(product);
            });
        }
    }

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES) //5 minutes
    public void syncProductUpdate() {
        List<Product> products = productRepository.findProductUpdatedRecently();
        for (Product product : products) {
            executorService.submit(() -> {
                kafkaProducer.sendProduct(product);
            });
        }

    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS) // 1 day
    public void syncRelatedProducts() {
        Set<Object> productIds = redisTemplate.opsForHash().keys("products");
        for (Object id : productIds) {
            List<Integer> relatedProducts = productRepository.findRelatedProductsLimit5((int) id);
            executorService.submit(() -> {
                kafkaProducer.sendRelatedProduct(new ProductRelated((int) id, relatedProducts));
            });
        }
    }


//    @Scheduled(fixedRate = 1000 * 60 * 60 * 24) // 1 day
//    public void syncProductAllFromDbToRedis() {
//        int size = 12;
//        int countProduct = productService.getCountProduct(0) | 0;
//        int totalPages = (int) Math.ceil((double) countProduct / size);
//        for (int i = 0; i < totalPages; i++) {
//            final int page = i;
//            String productAllKey = PRODUCT_ALL_PREFIX + page + size;
//            executorService.submit(() -> {
//                Pageable pageable = PageRequest.of(page, size);
//                Page<Product> products = productRepository.findAll(pageable);
//                long longExpires = 24 * 60 * 60 + (long) (Math.random() * 10 * 60);
//                try {
//                    String jsonData = objectMapper.writeValueAsString(products);
//                    redisTemplate.opsForValue().set(productAllKey, jsonData, longExpires, TimeUnit.SECONDS);
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }
//    }

//    @Scheduled(fixedRate = 1000 * 60 * 60 * 24) // 1 day
//    public void syncProductAllByCategory() {
//        try {
//            int size = 12;
//            List<Category> categories = categoryService.getAllCategories();
//            for (Category category : categories) {
//                int page = 0;
//                long longExpires = 24 * 60 * 60 + (long) (Math.random() * 10 * 60);
//                executorService.submit(() -> {
//                    Pageable pageable = PageRequest.of(page, size);
//                    String productKey = PRODUCT_ALL_PREFIX + page + "+" + size + category.getId();
//                    Page<Product> products = productRepository.getAllProductsByCategory(pageable, category.getId());
//                    String jsonData = null;
//                    try {
//                        jsonData = objectMapper.writeValueAsString(products);
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException(e);
//                    }
//                    redisTemplate.opsForValue().set(productKey, jsonData, longExpires, TimeUnit.SECONDS);
//
//                });
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Scheduled(fixedRate = 1000 * 60 * 60 * 24) // 1 day
//    public void syncProductHomePageFromDbToRedis() {
//        final int page = 0;
//        final int size = 10;
//        String productAllKey = PRODUCT_ALL_PREFIX + page + "+" + size;
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Product> products = productRepository.findAll(pageable);
//        long longExpires = 24 * 60 * 60 + (long) (Math.random() * 10 * 60);
//        String jsonData = null;
//        try {
//            jsonData = objectMapper.writeValueAsString(products);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        redisTemplate.opsForValue().set(productAllKey, jsonData, longExpires, TimeUnit.SECONDS);
//    }


}
