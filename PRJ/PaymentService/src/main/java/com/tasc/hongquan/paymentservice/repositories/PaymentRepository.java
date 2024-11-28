package com.tasc.hongquan.paymentservice.repositories;

import com.tasc.hongquan.paymentservice.dto.PaymentDataDTO;
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

    @Query(value = "SELECT user_id FROM orders o INNER JOIN payments p ON p.order_id = o.order_id WHERE p.payment_id =:paymentId", nativeQuery = true)
    String getUserIdByPaymentId(String paymentId);

    @Query(value = "SELECT p.payment_id, p.order_id FROM payments p WHERE payment_status = 'pending' AND TIMESTAMPDIFF(MINUTE, created_at, CURRENT_TIMESTAMP) > 5;", nativeQuery = true)
    List<PaymentDataDTO> finalAllTransactionPendingAndTimeLongerThan5Minutes();

}
