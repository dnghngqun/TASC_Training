package com.tasc.hongquan.orderservice.services;

import com.tasc.hongquan.orderservice.models.Order;
import com.tasc.hongquan.orderservice.models.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    void addOrder(Order order);
    Order updateOrderById(int id, Order order);
    void deleteOrderById(int id);
    Object addOrderWithDetailsUsingProcedure(String userId, BigDecimal totalPrice, Integer discountId, List<OrderDetail> orderDetails, String note, Integer addressId, String paymentMethod);
}
