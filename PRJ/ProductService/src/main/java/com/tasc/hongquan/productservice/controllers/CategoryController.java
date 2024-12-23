package com.tasc.hongquan.productservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tasc.hongquan.productservice.dto.ResponseBody;
import com.tasc.hongquan.productservice.models.Category;
import com.tasc.hongquan.productservice.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
@Slf4j
public class CategoryController {
    private CategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @PostMapping("/add")
    public ResponseEntity<ResponseBody> addCategory(@RequestBody Category category) {
        try {
            categoryService.add(category);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Category added successfully.", category)
            );
        } catch (Exception e) {
            logger.error("Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Error adding category.", null)
            );
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseBody> deleteCategory(@RequestBody int id) {
        try {
            categoryService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Category deleted successfully.", null)
            );
        } catch (Exception e) {
            logger.error("Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Error deleting category.", null)
            );
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseBody> updateCategory(@RequestBody Category category) {
        try {
            categoryService.update(category);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Category updated successfully.", category)
            );
        } catch (Exception e) {
            logger.error("Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Error updating category.", null)
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<ResponseBody> getAllCategory() {
        List<Category> categories = null;
        try {
            categories = categoryService.getAllCategories();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "No category found.", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseBody("ok", "Category found.", categories)
        );
    }
}
