package com.tasc.hongquan.paymentservice.repositories;

import com.tasc.hongquan.paymentservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUserId(String userId);
}
