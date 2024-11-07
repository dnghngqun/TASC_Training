package com.tasc.hongquan.productservice.dao.statement;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
@AllArgsConstructor
public class ProductDAOImpl implements ProductDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public int getCountProduct() {
        String sql = "SELECT COUNT(*) FROM Product";
        int count = namedParameterJdbcTemplate.queryForObject(sql, new HashMap<>(), Integer.class);
        return count;

    }
}
