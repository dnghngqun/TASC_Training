package com.tasc.hongquan.gomsuserver.services;

import com.tasc.hongquan.gomsuserver.models.Token;
import com.tasc.hongquan.gomsuserver.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Transactional
    public void saveToken(Token token) {
        try {
            token.setCreatedAt(Instant.now());
            token.setIsRevoked(false);

            tokenRepository.save(token);
        } catch (Exception e) {
            throw new RuntimeException("Error saving token: " + e.getMessage());
        }
    }

    public void revokeToken(String token) {
        try {
            Token existingToken = tokenRepository.findByToken(token);
            if (existingToken == null) {
                throw new RuntimeException("Token not found");
            }
            existingToken.setIsRevoked(true);
            tokenRepository.save(existingToken);
        } catch (Exception e) {
            throw new RuntimeException("Error revoking token: " + e.getMessage());
        }
    }
}
