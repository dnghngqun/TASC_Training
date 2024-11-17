package com.tasc.hongquan.orderservice.repositories;

import com.tasc.hongquan.orderservice.models.Order;
import com.tasc.hongquan.orderservice.models.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public Order addOrderWithDetailsUsingProcedure(String userId, BigDecimal totalPrice, Integer discountId, List<OrderDetail> orderDetails, String note, Integer addressId) {
        // Khởi tạo ObjectMapper để chuyển đổi List thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String orderDetailsJson = "";

        try {
            //convert list to JSON
            orderDetailsJson = objectMapper.writeValueAsString(orderDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("add_order_with_details")
                .returningResultSet("order", new BeanPropertyRowMapper<>(Order.class));

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("totalPrice", totalPrice);
        params.put("discountId", discountId);
        params.put("orderDetails", orderDetailsJson);
        params.put("noteRq", note);
        params.put("addressBookId", addressId);

        Map<String, Object> result = jdbcCall.execute(params);
        return (Order) result.get("order");
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
