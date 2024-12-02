package com.tasc.hongquan.paymentservice.repositories;

import com.tasc.hongquan.paymentservice.models.EmailRetryQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRetryQueueRepository extends JpaRepository<EmailRetryQueue, Integer> {

}
