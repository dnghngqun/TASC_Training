package com.tasc.hongquan.paymentservice.repositories;

import com.tasc.hongquan.paymentservice.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String > {
    List<Payment> findAll();
    @Query(value = "SELECT * FROM payment WHERE order_id =:orderId", nativeQuery = true)
    Payment findByOrderId(@Param("orderId") String orderId);
}
