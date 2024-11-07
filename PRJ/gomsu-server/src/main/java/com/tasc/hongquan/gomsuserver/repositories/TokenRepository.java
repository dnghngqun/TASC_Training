package com.tasc.hongquan.gomsuserver.repositories;

import com.tasc.hongquan.gomsuserver.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t WHERE t.token = ?1 AND t.user.userId = ?2 AND t.isRevoked = false")
    Token findByToken(int token, String userId);

    @Query("SELECT t FROM Token t WHERE t.user.userId = ?1 AND t.isRevoked = false")
    List<Token> getAllWhereUserIdAndIsRevoked(int userId);

    @Query("SELECT t FROM Token t WHERE t.user.userId = ?1 AND t.isRevoked = false AND t.expiresAt > current_timestamp() ORDER BY t.expiresAt DESC")
    Token findLatestValidTokenByUserId(String userId);

    @Query("select t from Token t where t.isRevoked = false")
    List<Token> getAllWhereIsRevoked();

    //return false if token is valid
    @Query(value = "SELECT CASE WHEN COUNT(t) > 0 THEN false ELSE true END FROM Token t " +
            "WHERE t.token = ?1 AND t.user.userId = ?2 AND (t.isRevoked = true OR t.expiresAt < current_timestamp())")
    Boolean checkTokenValid(int token, String userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expiresAt < current_timestamp() OR t.isRevoked = true")
    void deleteExpiredTokens();

}

