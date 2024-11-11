package com.tasc.hongquan.orderservice.services;

import com.tasc.hongquan.orderservice.models.OrderDetail;
import lombok.AllArgsConstructor;
import com.tasc.hongquan.orderservice.models.Order;
import org.springframework.stereotype.Service;
import com.tasc.hongquan.orderservice.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    @Override
    public void addOrder(Order order) {
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());
        orderRepository.addOrder(order);
    }


    @Override
    public void updateOrderById(int id, Order updateOrder) {
        Order order = orderRepository.findById(id);
        if(order == null){
            throw new RuntimeException("Order not found");
        }
        if(updateOrder.getUserId() != null){
            order.setUserId(updateOrder.getUserId());
        }
        if(updateOrder.getTotalPrice() != null){
            order.setTotalPrice(updateOrder.getTotalPrice());
        }
        if(updateOrder.getStatus() != null){
            order.setStatus(updateOrder.getStatus());
        }
        if(updateOrder.getDiscountId() != null){
            order.setDiscountId(updateOrder.getDiscountId());
        }
        order.setUpdatedAt(Instant.now());
        orderRepository.updateOrderById(order);
    }

    @Override
    public void deleteOrderById(int id) {
        orderRepository.deleteOrderById(id);
    }

    @Override
    public void addOrderWithDetailsUsingProcedure(String userId, BigDecimal totalPrice, Integer discountId, List<OrderDetail> orderDetails) {
        orderRepository.addOrderWithDetailsUsingProcedure(userId, totalPrice, discountId, orderDetails);
    }

}
