package com.tasc.hongquan.gomsuserver.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasc.hongquan.gomsuserver.DTO.LoginDTO;
import com.tasc.hongquan.gomsuserver.DTO.LoginResponse;
import com.tasc.hongquan.gomsuserver.DTO.RegisterDTO;
import com.tasc.hongquan.gomsuserver.Jwt.JwtTokenProvider;
import com.tasc.hongquan.gomsuserver.cookie.LoginCookie;
import com.tasc.hongquan.gomsuserver.models.CustomUserDetails;
import com.tasc.hongquan.gomsuserver.models.Token;
import com.tasc.hongquan.gomsuserver.models.User;
import com.tasc.hongquan.gomsuserver.services.TokenService;
import com.tasc.hongquan.gomsuserver.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authen;
    private final JwtTokenProvider jwtTokenProvider;
    private final ExecutorService executorService;
    private final ObjectMapper objectMapper;
    public static final int MAX_AGE = 3600;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, TokenService tokenService, AuthenticationManager authen, JwtTokenProvider jwt, ExecutorService executorService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authen = authen;
        this.jwtTokenProvider = jwt;
        this.executorService = executorService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody RegisterDTO user) {
        try {
            userService.createAccount(user, 3);
            return ResponseEntity.ok("Account created successfully");
        } catch (Exception ex) {
            logger.error("Error creating user: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasAnyAuthority('admin', 'superadmin')")
    public ResponseEntity<String> createAdmin(@RequestBody RegisterDTO user) {
        try {
            userService.createAccount(user, 1);
            return ResponseEntity.ok("Account admin created successfully");
        } catch (Exception ex) {
            logger.error("Error creating admin: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/shipper/register")
//    @PreAuthorize("hasAuthority('admin') or hasAuthority('user')")
//    @PreAuthorize("hasAnyAuthority('admin', 'user')")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<String> createShipper(@RequestBody RegisterDTO user) {
        try {
            userService.createAccount(user, 2);
            return ResponseEntity.ok("Account shipper created successfully");
        } catch (Exception ex) {
            logger.error("Error creating account: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/isLoggedIn")
    public boolean isLoggedIn(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return false;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("isLoggedIn") && cookie.getValue().equals("true")) {
                return true;
            }
        }
        return false;
    }


    @Transactional
    @PostMapping("/signin")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) throws Exception {
        try {
            Authentication authentication = authen.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        } catch (AuthenticationException authenticationException) {
            throw new Exception("Invalid username or password", authenticationException);
        }
        final CustomUserDetails userDetails = userService.loadUserByUsername(loginDTO.getEmail());
        final String jwt = jwtTokenProvider.generateToken(userDetails);
        final String email = String.valueOf(jwtTokenProvider.getEmailFromJWT(jwt));
        final String role = userService.getRoleName(email);
        final Date expiryDate = jwtTokenProvider.getExpirationDateFromJWT(jwt);

        User user = userService.getUserByEmail(email);
        System.out.println("User: " + user);

        //encode
        String encodeJWT = Base64.getEncoder().encodeToString(jwt.getBytes(StandardCharsets.UTF_8));
        String userJson = objectMapper.writeValueAsString(user);
        String encodeUser = Base64.getEncoder().encodeToString(userJson.getBytes(StandardCharsets.UTF_8));
//        String encodeJWT = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));

        //create a new cookie
        Cookie jwtCookie = new Cookie("jwt", encodeJWT);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(MAX_AGE); //1 hour
        jwtCookie.setHttpOnly(true);//not allow javascript access

        Cookie userCookie = new Cookie("user", encodeUser);
        userCookie.setPath("/");
        userCookie.setMaxAge(MAX_AGE); //1 hour

        Cookie roleCookie = new Cookie("role", role);
        roleCookie.setPath("/");
        roleCookie.setMaxAge(MAX_AGE); //1 hour

        Cookie isLoggedIn = new Cookie("isLoggedIn", "true");
        isLoggedIn.setPath("/");
        isLoggedIn.setMaxAge(MAX_AGE); //1 hour

        //save token
        executorService.submit(() -> {
            //save token to database
            tokenService.saveToken(new Token().builder()
                    .user(user)
                    .token(jwt)
                    .tokenType("JWT")
                    .expiresAt(expiryDate.toInstant())
                    .build()
            );
        });

        //add cookie to response
        response.addCookie(jwtCookie);
        response.addCookie(userCookie);
        response.addCookie(roleCookie);
        response.addCookie(isLoggedIn);


        return ResponseEntity.ok("Login success, JWT has been saved in cookie, role: " + role);
    }

    @Transactional
    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {

        //get cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    String jwtEncode = cookie.getValue();
//                    String jwtEncode = objectMapper.readValue(json, String.class);

                    byte[] decodedBytes = Base64.getDecoder().decode(jwtEncode);
                    String token = new String(decodedBytes);
                    tokenService.revokeToken(token);
                }
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }

            return "Logout success";
        }


        return "Logout failed";
    }
}
