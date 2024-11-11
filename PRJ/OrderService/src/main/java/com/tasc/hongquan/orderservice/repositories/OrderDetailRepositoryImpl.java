package com.tasc.hongquan.orderservice.repositories;

import com.tasc.hongquan.orderservice.models.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Repository
public class OrderDetailRepositoryImpl implements OrderDetailRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void addOrderDetail(OrderDetail orderDetail) {
        String sql = "INSERT INTO order_details (order_id, quantity, price, product_id, created_at, updated_at, deleted_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderDetail.getOrderId(), orderDetail.getQuantity(), orderDetail.getPrice(), orderDetail.getProductId(), orderDetail.getCreatedAt(), orderDetail.getUpdatedAt(), orderDetail.getDeletedAt());
    }

    @Override
    public void updateOrderDetail(OrderDetail orderDetail) {
        String sql = "UPDATE order_details SET order_id = ?, quantity = ?, price = ?, product_id = ?, created_at = ?, updated_at = ?, deleted_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderDetail.getOrderId(), orderDetail.getQuantity(), orderDetail.getPrice(), orderDetail.getProductId(), orderDetail.getCreatedAt(), orderDetail.getUpdatedAt(), orderDetail.getDeletedAt(), orderDetail.getId());
    }

    @Override
    public void deleteOrderDetail(int id) {
        String sql = "UPDATE order_details SET deleted_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, Instant.now(), id);
    }

    @Override
    public OrderDetail findById(int id) {
        String sql = "SELECT * FROM order_details WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapToOrderDetail, id);
    }

    @Override
    public List<OrderDetail> findByOrderId(int orderId) {
        String sql = "SELECT * FROM order_details WHERE order_id = ?";
        return jdbcTemplate.query(sql, this::mapToOrderDetail, orderId);
    }

    private OrderDetail mapToOrderDetail(ResultSet rs, int rowNum) throws SQLException {
        return new OrderDetail().builder()
                .id(rs.getInt("order_detail_id"))
                .orderId(rs.getInt("order_id"))
                .quantity(rs.getInt("quantity"))
                .price(rs.getBigDecimal("price"))
                .productId(rs.getInt("product_id"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .updatedAt(rs.getTimestamp("updated_at").toInstant())
                .deletedAt(rs.getTimestamp("deleted_at").toInstant())
                .build();
    }

    @Override
    public List<OrderDetail> getAllOrderDetail() {
        String sql = "SELECT * FROM order_details";
        return jdbcTemplate.query(sql, this::mapToOrderDetail);
    }
}
