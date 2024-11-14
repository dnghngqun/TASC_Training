package com.tasc.hongquan.productservice.controllers;

import com.tasc.hongquan.productservice.dto.ResponseBody;
import com.tasc.hongquan.productservice.models.Product;
import com.tasc.hongquan.productservice.services.ProductService;
import com.tasc.hongquan.productservice.services.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseBody> addProduct(@RequestBody Product product) {

        try {
            productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Product added successfully", product)
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Product not added", null)
            );
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseBody> getCountProduct(@RequestParam(value = "categoryId") String categoryIdParams) {
        try {
            Integer categoryId = Integer.parseInt(categoryIdParams);
            int count = productService.getCountProduct(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Product count", count)
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Product count not found", null)
            );
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseBody> getAllProduct(@RequestParam(value = "page", defaultValue = "0") String pageParams,
                                                      @RequestParam(value = "size", defaultValue = "12") String sizeParams, @RequestParam(value = "categoryId") String categoryIdParams) {
        try {
            int page = Integer.parseInt(pageParams);
            int size = Integer.parseInt(sizeParams);
            Integer categoryId = Integer.parseInt(categoryIdParams);
            Page<Product> products = productService.getAllProducts(page, size, categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Product found", products)
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Product not found", null)
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseBody> updateProduct(@RequestBody Product product) {
        try {
            productService.updateProduct(product);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Product updated successfully", product)
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Product not updated", null)
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseBody> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Product deleted successfully", null)
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Product not deleted", null)
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Product found", product)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseBody("error", "Product not found", null)
            );
        }
    }

    @GetMapping("/limit")
    public ResponseEntity<ResponseBody> getLimitNewProduct(@RequestParam("limit") int limit) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Product found", productService.getLimitNewProduct(limit))
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Product not found", null)
            );
        }
    }
}
