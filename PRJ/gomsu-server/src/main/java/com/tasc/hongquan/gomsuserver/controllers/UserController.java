package com.tasc.hongquan.gomsuserver.controllers;

import com.tasc.hongquan.gomsuserver.DTO.LoginDTO;
import com.tasc.hongquan.gomsuserver.DTO.LoginResponse;
import com.tasc.hongquan.gomsuserver.DTO.RegisterDTO;
import com.tasc.hongquan.gomsuserver.Jwt.JwtTokenProvider;
import com.tasc.hongquan.gomsuserver.models.CustomUserDetails;
import com.tasc.hongquan.gomsuserver.models.User;
import com.tasc.hongquan.gomsuserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authen;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authen, JwtTokenProvider jwt) {
        this.userService = userService;
        this.authen = authen;
        this.jwtTokenProvider = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody RegisterDTO user) {
        return ResponseEntity.ok(userService.createAccount(user, 3));
    }

    @PostMapping("/shipper/register")
    public ResponseEntity<User> createShipper(@RequestBody RegisterDTO user) {
        return ResponseEntity.ok(userService.createAccount(user, 2));
    }

    @GetMapping("/signin")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO loginDTO) throws Exception {
        try {
            Authentication authentication = authen.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        } catch (AuthenticationException authenticationException) {
            throw new Exception("Invalid username or password", authenticationException);
        }
        final CustomUserDetails userDetails = userService.loadUserByUsername(loginDTO.getEmail());
        final String jwt = jwtTokenProvider.generateToken(userDetails);
        final String UUID = String.valueOf(jwtTokenProvider.getEmailFromJWT(jwt));
        final String role = userService.getRoleName(UUID);
        return ResponseEntity.ok(new LoginResponse(jwt, UUID, role));
    }

}
