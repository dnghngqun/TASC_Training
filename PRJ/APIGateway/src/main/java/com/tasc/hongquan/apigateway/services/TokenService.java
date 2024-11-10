package com.tasc.hongquan.apigateway.services;

import com.tasc.hongquan.apigateway.models.Token;

public interface TokenService {
    Token findByToken(int token, String userId);

    void saveToken(Token token);

    int generateOTP();

    void revokeToken(int token, String userId);

    boolean validateToken(int token, String userId);

    boolean checkValidOTP(int otp, String userId);
}
