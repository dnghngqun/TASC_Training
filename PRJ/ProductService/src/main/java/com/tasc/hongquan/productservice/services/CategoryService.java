package com.tasc.hongquan.productservice.services;

import com.tasc.hongquan.productservice.models.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    void add(Category category);

    void delete(int id);

    void update(Category category);

    Category getCategoryById(int id);
}
