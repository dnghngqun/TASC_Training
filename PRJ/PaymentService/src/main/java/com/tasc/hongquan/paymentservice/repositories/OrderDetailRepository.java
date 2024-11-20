package com.tasc.hongquan.paymentservice.repositories;

import com.tasc.hongquan.paymentservice.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query(value = "select order_detail_id, quantity from order_details where order_id =:orderId", nativeQuery = true)
    List<Object[]> getMapOrderDetailIdAndQuantityByOrderId(@Param("orderId") int orderId);
}
