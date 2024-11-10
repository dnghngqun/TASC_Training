package com.tasc.hongquan.apigateway.services;


import com.tasc.hongquan.apigateway.models.CustomUserDetails;
import com.tasc.hongquan.apigateway.models.User;
import com.tasc.hongquan.apigateway.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = Optional.of(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email)));

        // Ở đây ánh xạ User vào đối tượng UserDetails của Spring Security
        // same as method findUserByEmail
        return new CustomUserDetails(userOptional.get());
    }

}
