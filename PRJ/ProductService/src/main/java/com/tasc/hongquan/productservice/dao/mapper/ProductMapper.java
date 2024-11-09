package com.tasc.hongquan.productservice.dao.mapper;

import com.tasc.hongquan.productservice.models.Category;
import com.tasc.hongquan.productservice.models.Product;
import org.springframework.jdbc.core.RowMapper;

public class ProductMapper {
    public RowMapper<Product> productRowMapper = (((rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setDescription(rs.getString("description"));
        product.setImageUrl(rs.getString("image_url"));
        product.setStock(rs.getInt("stock"));
        Category category = new Category();
        category.setId(rs.getInt("category_id"));
        product.setCategory(category);
        return product;
    }));
}

