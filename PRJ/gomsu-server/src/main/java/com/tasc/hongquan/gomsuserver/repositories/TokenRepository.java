package com.tasc.hongquan.gomsuserver.repositories;

import com.tasc.hongquan.gomsuserver.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(int token);

    @Query("SELECT t FROM Token t WHERE t.user.userId = ?1 AND t.isRevoked = false")
    List<Token> getAllWhereUserIdAndIsRevoked(int userId);

    @Query("select t from Token t where t.isRevoked = false")
    List<Token> getAllWhereIsRevoked();

}

