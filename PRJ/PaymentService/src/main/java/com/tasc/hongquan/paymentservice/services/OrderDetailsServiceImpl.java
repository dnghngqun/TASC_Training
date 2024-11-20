package com.tasc.hongquan.paymentservice.services;

import com.tasc.hongquan.paymentservice.models.OrderDetail;
import com.tasc.hongquan.paymentservice.repositories.OrderDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public void updateOrderDetailStatus(int orderDetailId, String status) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(() -> new RuntimeException("Order detail not found"));
        orderDetail.setStatus(status);
        orderDetailRepository.save(orderDetail);
    }
}
