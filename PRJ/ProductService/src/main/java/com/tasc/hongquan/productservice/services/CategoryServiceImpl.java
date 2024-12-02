package com.tasc.hongquan.productservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasc.hongquan.productservice.models.Category;
import com.tasc.hongquan.productservice.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private CategoryRepository categoryRepository;

    @Override
    public void add(Category category) {
        category.setCreatedAt(Instant.now());
        category.setUpdatedAt(Instant.now());
        categoryRepository.save(category);
    }

    @Override
    public void delete(int id) {
        categoryRepository.findById(id).ifPresent(category -> categoryRepository.delete(category));
    }

    @Override
    public void update(Category categoryUpdate) {
        Category category = categoryRepository.findById(categoryUpdate.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if (categoryUpdate.getName() != null) {
            category.setName(categoryUpdate.getName());
        }
        category.setUpdatedAt(Instant.now());
        categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(int id) {
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
