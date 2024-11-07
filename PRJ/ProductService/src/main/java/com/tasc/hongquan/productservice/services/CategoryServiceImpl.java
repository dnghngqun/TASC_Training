package com.tasc.hongquan.productservice.services;

import com.tasc.hongquan.productservice.models.Category;
import com.tasc.hongquan.productservice.repositories.CategoryRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor

public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    @Override
    public void add(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void delete(int id) {
        categoryRepository.findById(id).ifPresent(category -> categoryRepository.delete(category));
    }

    @Override
    public void update(Category categoryUpdate) {
        Category category = new Category();
        if (categoryUpdate.getName() != null) {
            category.setName(categoryUpdate.getName());
        }
        categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(int id) {
        return null;
    }
}
