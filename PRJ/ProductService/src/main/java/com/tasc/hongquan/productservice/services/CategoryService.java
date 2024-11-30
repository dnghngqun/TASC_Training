package com.tasc.hongquan.productservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tasc.hongquan.productservice.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    void add(Category category);

    void delete(int id);

    void update(Category category);

    Category getCategoryById(int id);

    List<Category> getAllCategories() throws JsonProcessingException;
}
