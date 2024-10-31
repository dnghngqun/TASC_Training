package com.tasc.hongquan.gomsuserver.repositories;

import com.tasc.hongquan.gomsuserver.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
}
