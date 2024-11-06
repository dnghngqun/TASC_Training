package com.tasc.hongquan.gomsuserver.services;

import com.tasc.hongquan.gomsuserver.models.Token;
import com.tasc.hongquan.gomsuserver.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token findByToken(int token, String userId) {
        return tokenRepository.findByToken(token, userId);
    }

    @Transactional
    public void saveToken(Token token) {
        try {

            token.setCreatedAt(Instant.now());
            token.setIsRevoked(false);
            //5 minutes otp
            token.setExpiresAt(Instant.now().plusSeconds(300));
            tokenRepository.save(token);
        } catch (Exception e) {
            throw new RuntimeException("Error saving token: " + e.getMessage());
        }
    }

    public int generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(90000000) + 10000000;
        return otp;
    }

    public void revokeToken(int token, String userId) {
        try {
            Token existingToken = tokenRepository.findByToken(token, userId);
            if (existingToken == null) {
                System.out.println("Token not found to revoked");
            }
            existingToken.setIsRevoked(true);
            tokenRepository.save(existingToken);
        } catch (Exception e) {
            throw new RuntimeException("Error revoking token: " + e.getMessage());
        }
    }

    public boolean validateToken(int token, String userId) {
        return tokenRepository.checkTokenValid(token, userId);

    }

    public boolean checkValidOTP(int otp, String userId) {
        return tokenRepository.checkTokenValid(otp, userId);
    }
}
