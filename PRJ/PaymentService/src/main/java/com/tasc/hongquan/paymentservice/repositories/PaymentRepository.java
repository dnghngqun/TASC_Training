package com.tasc.hongquan.paymentservice.repositories;

import com.tasc.hongquan.paymentservice.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer > {
    List<Payment> findAll();
}
