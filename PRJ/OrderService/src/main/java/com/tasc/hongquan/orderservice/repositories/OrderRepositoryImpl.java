package com.tasc.hongquan.orderservice.repositories;

import com.tasc.hongquan.orderservice.models.Order;
import com.tasc.hongquan.orderservice.models.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepositoryImpl implements OrderRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Order> getAllOrders(){
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, this::mapRowToOrder);
    }

    public void addOrder(Order order){
        String sql = "INSERT INTO orders (user_id, total_price, status, discount_id, created_at, updated_at, deleted_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.getUserId(), order.getTotalPrice(), order.getStatus(), order.getDiscountId(), order.getCreatedAt(), order.getUpdatedAt(), order.getDeletedAt());
    }

    public void updateOrderById( Order order){
        String sql = "UPDATE orders SET user_id = ?, total_price = ?, status = ?, discount_id = ?, created_at = ?, updated_at = ?, deleted_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, order.getUserId(), order.getTotalPrice(), order.getStatus(), order.getDiscountId(), order.getCreatedAt(), order.getUpdatedAt(), order.getDeletedAt(), order.getId());
    }

    @Override
    public void deleteOrderById(int id) {
        String sql = "UPDATE orders SET deleted_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, Instant.now(), id);
    }

    @Override
    public Order findById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToOrder, id);
    }
    @Override
    public void addOrderWithDetailsUsingProcedure(String userId, BigDecimal totalPrice, Integer discountId, List<OrderDetail> orderDetails) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("add_order_with_details");

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("total_price", totalPrice);
        params.put("discount_id", discountId);
        params.put("order_details", orderDetails);

        jdbcCall.execute(params);
    }

    private Order mapRowToOrder(ResultSet rs, int rowNum) throws SQLException {
        return new Order().builder()
                .id(rs.getInt("order_id"))
                .userId(rs.getString("user_id"))
                .totalPrice(rs.getBigDecimal("total_price"))
                .status(rs.getString("status"))
                .discountId(rs.getInt("discount_id"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .updatedAt(rs.getTimestamp("updated_at").toInstant())
                .deletedAt(rs.getTimestamp("deleted_at").toInstant())
                .build();
    }
}
