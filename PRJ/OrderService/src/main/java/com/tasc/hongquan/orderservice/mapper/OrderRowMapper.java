package com.tasc.hongquan.orderservice.mapper;

import com.tasc.hongquan.orderservice.models.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Order().builder()
                .id(rs.getInt("order_id"))
                .userId(rs.getString("user_id"))
                .addressId(rs.getInt("address_book_id"))
                .totalPrice(rs.getBigDecimal("total_price"))
                .status(rs.getString("status"))
                .discountId(rs.getInt("discount_id"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .updatedAt(rs.getTimestamp("updated_at").toInstant())
                .deletedAt(rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toInstant() : null)
                .build();
    }
}

