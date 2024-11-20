package com.tasc.hongquan.productservice.services;

import com.tasc.hongquan.productservice.dao.statement.ProductDAO;
import com.tasc.hongquan.productservice.dao.statement.ProductDAOImpl;
import com.tasc.hongquan.productservice.models.Product;
import com.tasc.hongquan.productservice.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductDAO productDAO;
    private final int numThread = 4;
    private final List<Future<?>> futures = new ArrayList<>();
    private final ExecutorService excecutorService = Executors.newFixedThreadPool(numThread);
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductDAOImpl productDAO) {
        this.productRepository = productRepository;
        this.productDAO = productDAO;
    }

    @Override
    public List<Product> getLimitNewProduct(int limit) {
        return productDAO.getLimitNewProduct(limit);
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
    public Page<Product> getAllProducts(int page, int size, Integer categoryId) {
        Pageable pageable = PageRequest.of(page, size);
        if (categoryId != null && categoryId > 0) {
            System.out.println("Category id: " + categoryId);
            return productRepository.getAllProductsByCategory(pageable, categoryId);
        }
        return productRepository.findAll(pageable);
    }
}
