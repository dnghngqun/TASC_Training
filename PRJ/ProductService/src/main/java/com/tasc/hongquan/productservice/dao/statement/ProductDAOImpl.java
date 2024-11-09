package com.tasc.hongquan.productservice.dao.statement;

import com.tasc.hongquan.productservice.dao.mapper.ProductMapper;
import com.tasc.hongquan.productservice.models.Product;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@AllArgsConstructor
public class ProductDAOImpl implements ProductDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public int getCountProduct() {
        String sql = "SELECT COUNT(*) FROM products";
        int count = namedParameterJdbcTemplate.queryForObject(sql, new HashMap<>(), Integer.class);
        return count;

    }

    public int getCountProductWhereCategoryId(int categoryId) {
        String sql = "SELECT COUNT(*) FROM products where category_id = " + categoryId;
        int count = namedParameterJdbcTemplate.queryForObject(sql, new HashMap<>(), Integer.class);
        return count;
    }

    public List<Product> getLimitNewProduct(int limit) {
        String sql = "SELECT * FROM products ORDER BY created_at DESC LIMIT " + limit;
        List<Product> products = namedParameterJdbcTemplate.query(sql, new HashMap<>(), new ProductMapper().productRowMapper);
        return products;
    }
}
