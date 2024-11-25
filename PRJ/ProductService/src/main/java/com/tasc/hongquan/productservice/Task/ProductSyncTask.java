package com.tasc.hongquan.productservice.Task;

import com.tasc.hongquan.productservice.dao.statement.ProductDAO;
import com.tasc.hongquan.productservice.models.Category;
import com.tasc.hongquan.productservice.repositories.ProductRepository;
import com.tasc.hongquan.productservice.services.CategoryService;
import com.tasc.hongquan.productservice.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@AllArgsConstructor
public class ProductSyncTask {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final CategoryService categoryService;

    @Qualifier("redisTemplate")
    private final RedisTemplate redisTemplate;

    @Scheduled(fixedRate = 1000 * 60 * 10) // 10 minutes
    public void syncProductAllFromDbToRedis() {
        List<Category> categories = categoryService.getAllCategories();
        for (Category category : categories) {
            productService.getCountProduct(category.getId());
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 15) // 15 minutes
    public void syncProductHomePageFromDbToRedis() {

    }
}
