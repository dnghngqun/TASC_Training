package com.tasc.hongquan.orderservice.repositories;

import com.tasc.hongquan.orderservice.models.Discount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Repository
public class DiscountRepositoryImpl implements DiscountRepository {
    private JdbcTemplate jdbcTemplate;
    @Override
    public void addDiscount(Discount discount) {
        String sql = "INSERT INTO discounts (code, amount, quantity, expires, description, created_at, updated_at, deleted_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, discount.getCode(), discount.getAmount(), discount.getQuantity(), discount.getExpires(), discount.getDescription(), discount.getCreatedAt(), discount.getUpdatedAt(), discount.getDeletedAt());
    }

    @Override
    public void updateDiscount(Discount discount) {
        String sql = "UPDATE discounts SET code = ?, amount = ?, quantity = ?, expires = ?, description = ?, created_at = ?, updated_at = ?, deleted_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, discount.getCode(), discount.getAmount(), discount.getQuantity(), discount.getExpires(), discount.getDescription(), discount.getCreatedAt(), discount.getUpdatedAt(), discount.getDeletedAt(), discount.getId());
    }

    @Override
    public void deleteDiscount(int id) {
        String sql = "UPDATE discounts SET deleted_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, Instant.now(), id);
    }

    @Override
    public Discount findById(int id) {
        String sql = "SELECT * FROM discounts WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapToDiscount, id);
    }

    @Override
    public Discount findByCode(String code) {
        String sql = "SELECT * FROM discounts WHERE code = ?";
        return jdbcTemplate.queryForObject(sql, this::mapToDiscount, code);
    }

    @Override
    public List<Discount> getAllDiscounts() {
        String sql = "SELECT * FROM discounts";
        return jdbcTemplate.query(sql, this::mapToDiscount);
    }

    private Discount mapToDiscount(ResultSet rs, int rowNum) throws SQLException {
        return new Discount().builder()
                .id(rs.getInt("discount_id"))
                .code(rs.getString("code"))
                .amount(rs.getBigDecimal("amount"))
                .quantity(rs.getInt("quantity"))
                .expires(rs.getTimestamp("expires").toInstant())
                .description(rs.getString("description"))
                .updatedAt(rs.getTimestamp("updated_at").toInstant())
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .deletedAt(rs.getTimestamp("deleted_at").toInstant())
                .build();
    }
}
