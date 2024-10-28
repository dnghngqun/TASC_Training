package com.tasc.hongquan.gomsuserver.repositories;

import com.tasc.hongquan.gomsuserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
