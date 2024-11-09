package com.tasc.hongquan.productservice.repositories;

import com.tasc.hongquan.productservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
