package com.tasc.hongquan.orderservice.services;

import com.tasc.hongquan.orderservice.client.PaymentClient;
import com.tasc.hongquan.orderservice.dto.ResponseObject;
import com.tasc.hongquan.orderservice.models.OrderDetail;
import com.tasc.hongquan.orderservice.models.Payment;
import lombok.AllArgsConstructor;
import com.tasc.hongquan.orderservice.models.Order;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.tasc.hongquan.orderservice.repositories.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private PaymentClient paymentClient;
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
    @Transactional
    public Object addOrderWithDetailsUsingProcedure(String userId, BigDecimal totalPrice, Integer discountId, List<OrderDetail> orderDetails, String note, Integer addressId, String paymentMethod) {
        Order order = orderRepository.addOrderWithDetailsUsingProcedure(userId, totalPrice, discountId, orderDetails, note, addressId);
        try {
            Payment payment = new Payment().builder()
                    .order(order)
                    .paymentMethod(paymentMethod)
                    .build();
            ResponseEntity<ResponseObject> response = paymentClient.addPayment(payment);
            logger.info("Payment response: " + response);
            if(response.getStatusCode().is2xxSuccessful()) {
                order.setStatus("success");
                return response.getBody().getData();
            }
            else {
                order.setStatus("error");
                return null;
            }
        }catch(Exception exception) {
            throw new RuntimeException("Payment failed");

        }
    }

}
