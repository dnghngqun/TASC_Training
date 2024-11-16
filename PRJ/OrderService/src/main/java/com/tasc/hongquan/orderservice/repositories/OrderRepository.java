package com.tasc.hongquan.orderservice.repositories;

import com.tasc.hongquan.orderservice.models.Order;
import com.tasc.hongquan.orderservice.models.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository {
    List<Order> getAllOrders();
    void addOrder(Order order);
    void updateOrderById( Order order);
    void deleteOrderById(int id);
    Order findById(int id);
    void addOrderWithDetailsUsingProcedure(String userId, BigDecimal totalPrice, Integer discountId, List<OrderDetail> orderDetails, String note, Integer addressId);
}
