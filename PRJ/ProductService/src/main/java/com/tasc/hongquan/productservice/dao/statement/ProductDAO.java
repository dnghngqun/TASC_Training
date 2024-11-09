package com.tasc.hongquan.productservice.dao.statement;

import com.tasc.hongquan.productservice.models.Product;

import java.util.List;

public interface ProductDAO {
    int getCountProduct();

    int getCountProductWhereCategoryId(int categoryId);

    List<Product> getLimitNewProduct(int limit);
}
