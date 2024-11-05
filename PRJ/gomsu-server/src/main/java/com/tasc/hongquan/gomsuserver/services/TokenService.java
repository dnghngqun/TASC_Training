package com.tasc.hongquan.gomsuserver.services;

import com.tasc.hongquan.gomsuserver.models.Token;
import com.tasc.hongquan.gomsuserver.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token findByToken(int token) {
        return tokenRepository.findByToken(token);
    }

    @Transactional
    public void saveToken(Token token) {
        try {
            token.setCreatedAt(Instant.now());
            token.setTokenType("OTP");
            token.setIsRevoked(false);
            //5 minutes otp
            token.setExpiresAt(Instant.now().plusSeconds(300));
            tokenRepository.save(token);
        } catch (Exception e) {
            throw new RuntimeException("Error saving token: " + e.getMessage());
        }
    }

    public String generateOTP() {
        List<String> numbers = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        Collections.shuffle(numbers, new SecureRandom());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            builder.append(numbers.get(i));
        }
        return builder.toString();
    }

    public void revokeToken(int token) {
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
