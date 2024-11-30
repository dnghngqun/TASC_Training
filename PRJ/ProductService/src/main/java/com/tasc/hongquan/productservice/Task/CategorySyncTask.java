package com.tasc.hongquan.productservice.Task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasc.hongquan.productservice.models.Category;
import com.tasc.hongquan.productservice.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.tasc.hongquan.productservice.services.CategoryServiceImpl.CATEGORY_KEY;

@Component
@AllArgsConstructor
public class CategorySyncTask {
    private final CategoryService categoryService;
    @Qualifier("redisTemplate")
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void syncCategory() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            redisTemplate.opsForValue().set(CATEGORY_KEY, objectMapper.writeValueAsString(categories));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
