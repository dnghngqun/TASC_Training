package com.tasc.hongquan.orderservice.services;

import com.tasc.hongquan.orderservice.models.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    void addOrderDetail(OrderDetail orderDetail);
    void updateOrderDetail(OrderDetail orderDetail);
    void deleteOrderDetail(int id);
    OrderDetail findById(int id);
    List<OrderDetail> findByOrderId(int orderId);
    List<OrderDetail> getAllOrderDetail();

}
