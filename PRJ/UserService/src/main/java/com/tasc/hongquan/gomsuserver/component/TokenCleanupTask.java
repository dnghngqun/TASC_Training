package com.tasc.hongquan.gomsuserver.component;

import com.tasc.hongquan.gomsuserver.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupTask {
    @Autowired
    private TokenRepository tokenRepository;


    @Scheduled(cron = "0 0 * * * *")
    public void cleanupTokens() {
        tokenRepository.deleteExpiredTokens();
    }
    
}
