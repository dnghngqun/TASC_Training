package com.tasc.hongquan.orderservice.services;

import com.tasc.hongquan.orderservice.models.OrderDetail;
import com.tasc.hongquan.orderservice.repositories.OrderDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetail.setCreatedAt(Instant.now());
        orderDetail.setUpdatedAt(Instant.now());
        orderDetailRepository.addOrderDetail(orderDetail);
    }

    @Override
    public void updateOrderDetail(OrderDetail orderDetailUpdate) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailUpdate.getId());
        if(orderDetail == null){
            throw new RuntimeException("OrderDetail not found");
        }
        if(orderDetailUpdate.getOrderId() != null){
            orderDetail.setOrderId(orderDetailUpdate.getOrderId());
        }
        if(orderDetailUpdate.getProductId() != null){
            orderDetail.setProductId(orderDetailUpdate.getProductId());
        }
        if(orderDetailUpdate.getQuantity() != null){
            orderDetail.setQuantity(orderDetailUpdate.getQuantity());
        }
        if(orderDetailUpdate.getPrice() != null){
            orderDetail.setPrice(orderDetailUpdate.getPrice());
        }
        orderDetail.setUpdatedAt(Instant.now());
        orderDetailRepository.updateOrderDetail(orderDetail);
    }

    @Override
    public void deleteOrderDetail(int id) {
        orderDetailRepository.deleteOrderDetail(id);
    }

    @Override
    public OrderDetail findById(int id) {
        return orderDetailRepository.findById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(int orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public List<OrderDetail> getAllOrderDetail() {
        return orderDetailRepository.getAllOrderDetail();
    }
}
