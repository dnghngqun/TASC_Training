package com.tasc.hongquan.orderservice.repositories;

import com.tasc.hongquan.orderservice.mapper.OrderRowMapper;
import com.tasc.hongquan.orderservice.models.Order;
import com.tasc.hongquan.orderservice.models.OrderDetail;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Slf4j
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderRowMapper orderRowMapper = new OrderRowMapper();
    private final Logger logger = LoggerFactory.getLogger(OrderRepositoryImpl.class);

    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, orderRowMapper);
    }

    public void addOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, total_price, status, discount_id,address_book_id,note, created_at, updated_at, deleted_at) VALUES (?,?,?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.getUserId(), order.getTotalPrice(), order.getStatus(), order.getDiscountId(), order.getAddressId(), order.getNote(), order.getCreatedAt(), order.getUpdatedAt(), order.getDeletedAt());
    }

    public Order updateOrderById(Order order) {
        String sql = "UPDATE orders SET user_id = ?, total_price = ?, status = ?, discount_id = ?, created_at = ?, updated_at = ?, deleted_at = ? WHERE order_id = ?";
        int result = jdbcTemplate.update(sql, order.getUserId(), order.getTotalPrice(), order.getStatus(), order.getDiscountId(), order.getCreatedAt(), order.getUpdatedAt(), order.getDeletedAt(), order.getId());
        if (result == 1) {
            return order;
        }
        return null;
    }

    @Override
    public void deleteOrderById(int id) {
        String sql = "UPDATE orders SET deleted_at = ? WHERE order_id = ?";
        jdbcTemplate.update(sql, Instant.now(), id);
    }

    @Override
    public Order findById(int id) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        logger.info("Executing SQL: {} with id: {}", sql, id);
        return jdbcTemplate.queryForObject(sql, orderRowMapper, id);
    }


    @Override
    public Order addOrderWithDetailsUsingProcedure(String userId, BigDecimal totalPrice, Integer discountId, List<OrderDetail> orderDetails, String note, Integer addressId) {
        // Khởi tạo ObjectMapper để chuyển đổi List thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String orderDetailsJson = "";

        try {
            //convert list to JSON
            orderDetailsJson = objectMapper.writeValueAsString(orderDetails);
            logger.info("Order Details JSON: " + orderDetailsJson);
        } catch (Exception e) {
            logger.error("Error to convert order details to JSON: ", e);

        }

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("add_order_with_details")
                .returningResultSet("order", orderRowMapper);

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        logger.info("userId: {}", userId);
        params.put("totalPrice", totalPrice);
        logger.info("totalPrice: {}", totalPrice);

        params.put("discountId", discountId != null ? discountId : null);
        logger.info("discountId: {}", discountId);
        params.put("orderDetails", orderDetailsJson);
        logger.info("orderDetails: {}", orderDetailsJson);
        params.put("noteRq", note);
        logger.info("note: {}", note);
        params.put("addressBookId", addressId);
        logger.info("addressId: {}", addressId);

        Map<String, Object> result = jdbcCall.execute(params);

        logger.info("Rs: {}", result);
        List<Order> orders = (List<Order>) result.get("order");
        Order order = orders.get(0);
        System.out.println(order);
        return order;

    }


    private Order mapRowToOrder(ResultSet rs, int rowNum) throws SQLException {
        return new Order().builder()
                .id(rs.getInt("order_id"))
                .userId(rs.getString("user_id"))
                .addressId(rs.getInt("address_book_id"))
                .totalPrice(rs.getBigDecimal("total_price"))
                .status(rs.getString("status"))
                .discountId(rs.getInt("discount_id"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .updatedAt(rs.getTimestamp("updated_at").toInstant())
                .deletedAt(rs.getTimestamp("deleted_at").toInstant())
                .build();
    }
}
