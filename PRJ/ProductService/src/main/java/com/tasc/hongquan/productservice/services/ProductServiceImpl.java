package com.tasc.hongquan.productservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasc.hongquan.productservice.dao.statement.ProductDAO;
import com.tasc.hongquan.productservice.dao.statement.ProductDAOImpl;
import com.tasc.hongquan.productservice.models.Product;
import com.tasc.hongquan.productservice.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductDAO productDAO;
    private final int numThread = 4;
    private final List<Future<?>> futures = new ArrayList<>();
    private final ExecutorService excecutorService = Executors.newFixedThreadPool(numThread);
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    //key moi san pham se la product-page:
    public static final String PRODUCT_PAGE_KEY_PREFIX = "product_all:";
    public static double EXPIRES_TIME_CACHE = 10 * 60 + Math.ceil(Math.random() * 10);
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductDAOImpl productDAO, @Qualifier("redisTemplate") RedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.productDAO = productDAO;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }


    @Transactional
    @Override
    public List<Integer> updateStockProduct(Map<Integer, Integer> productStocks) throws ExecutionException, InterruptedException {
        //chia nho map
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(productStocks.entrySet());
        int chunkSize = (int) Math.ceil((double) entries.size() / numThread);
        List<Integer> productSuccess = new ArrayList<>();

        for (int i = 0; i < numThread; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, entries.size());
            //create sublist from start -> end
            List<Map.Entry<Integer, Integer>> sublist = entries.subList(start, end);
            //send task to thread
            futures.add(excecutorService.submit(() -> {
                for (Map.Entry<Integer, Integer> entry : sublist) {
                    Integer orderDetailId = entry.getKey();
                    Integer quantity = entry.getValue();
                    int result = updateStock(orderDetailId, quantity);
                    if (result == 0) {
                        synchronized (productSuccess) {
                            productSuccess.add(orderDetailId);
                        }
                    }

                }
            }));
            if (end == entries.size()) {
                break;
            }
        }
        for (Future<?> future : futures) {
            future.get();
        }
        return productSuccess;

    }

    private synchronized int updateStock(int orderDetailsId, int quantity) {
        Product product = productRepository.findByOrderDetailId(orderDetailsId).orElseThrow(() -> new RuntimeException("Product not found"));
        logger.info("Product: " + product.getName() + " - " + product.getStock());
        if (product.getStock() > 0) {
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
            return 0;
        }
        return orderDetailsId;
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
    public List<Product> getRelatedProducts(int productId) {
        return null;
    }

    //    @Override
//    public Page<Product> getAllProducts(int page, int size, Integer categoryId) throws JsonProcessingException {
//        Pageable pageable = PageRequest.of(page, size);
//        String productPageKey = categoryId != null && categoryId > 0
//                ? PRODUCT_PAGE_KEY_PREFIX + page + "+" + size + "+" + categoryId
//                : PRODUCT_PAGE_KEY_PREFIX + page + "+" + size;
//        //check cache
//        if (redisTemplate.hasKey(productPageKey)) {
//            String json = String.valueOf(redisTemplate.opsForValue().get(productPageKey));
//            return objectMapper.readValue(json, new TypeReference<PageImpl<Product>>() {
//            });
//        }
//        //no cache
//        Page<Product> products = categoryId != null && categoryId > 0
//                ? productRepository.getAllProductsByCategory(pageable, categoryId)
//                : productRepository.findAll(pageable);
//
//        String json = objectMapper.writeValueAsString(products);
//        redisTemplate.opsForValue().set(productPageKey, json, (long) EXPIRES_TIME_CACHE, TimeUnit.SECONDS);
//
//        return products;
//    }
    public List<Product> getAllProducts(int page, int size, int categoryId) {
        try {
            Set<Object> productIds = redisTemplate.opsForHash().keys("products");

            List<Product> products = redisTemplate.opsForHash().multiGet("products", productIds).stream()
                    .map(obj -> (Product) obj)
                    .collect(Collectors.toList());

            if (categoryId != 0) {
                products = products.stream()
                        .map(obj -> (Product) obj)
                        .filter(product -> product.getCategory().getId() == categoryId)
                        .collect(Collectors.toList());
            }

            int totalProduct = products.size();
            int start = (page - 1) * size;
            int end = Math.min(start + size, totalProduct);

            List<Product> pageProducts = products.subList(start, end);
            return pageProducts;
        } catch (Exception e) {
            log.error("Disconnect to Redis: " + e.getMessage());
            if (categoryId == 0) {
                Page<Product> products = productRepository.findAll(PageRequest.of(page, size));
                return products.getContent();
            }
            return productRepository.getAllProductsByCategory(PageRequest.of(page, size), categoryId).getContent();
        }
    }
}
