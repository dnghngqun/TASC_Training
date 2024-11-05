package com.tasc.hongquan.gomsuserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasc.hongquan.gomsuserver.DTO.LoginDTO;
import com.tasc.hongquan.gomsuserver.DTO.RegisterDTO;
import com.tasc.hongquan.gomsuserver.Jwt.JwtTokenProvider;
import com.tasc.hongquan.gomsuserver.models.CustomUserDetails;
import com.tasc.hongquan.gomsuserver.models.User;
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
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authen;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    public static final int MAX_AGE = 3600 * 24;// 1 day
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, AuthenticationManager authen, JwtTokenProvider jwt, ObjectMapper objectMapper) {
        this.userService = userService;
        this.authen = authen;
        this.jwtTokenProvider = jwt;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/public/register")
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

    @PostMapping("request-change-password")
    @PreAuthorize("hasAnyAuthority('admin', 'superadmin', 'shipper')")
    public ResponseEntity<String> requestChangePassword(@RequestParam String email) {
        try {
            //find user
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            //generate token otp
            String otp = UUID.randomUUID().toString();

        } catch (Exception e) {
            logger.error("Error requesting change password: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/public/oauth2/success")
    public ResponseEntity<String> googleLoginSuccess(HttpServletResponse response, OAuth2AuthenticationToken authentication) throws Exception {
        OAuth2User oauth2User = authentication.getPrincipal();


        String email = oauth2User.getAttribute("email");
        String providerId = authentication.getPrincipal().getAttribute("sub");
        String provider = "google";
        User user = userService.getUserByEmail(email);
        if (user == null) {
            String randomPassword = UUID.randomUUID().toString();
            user = new User();
            user.setFullName(oauth2User.getAttribute("name"));
            user.setEmail(email);
            user.setPassword(randomPassword);
            user.setProvider(provider);
            user.setProviderId(providerId);
            userService.createAccountWithGG(user, 3);
        }
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(user.getPassword());
        login(loginDTO, response);

        return ResponseEntity.ok("<html><body>" +
                "<script>" +
                "window.opener.postMessage(" +
                "{ email: '" + email + "' }, '*');" +
                "window.close();" +  // Đóng popup sau khi gửi dữ liệu
                "</script>" +
                "</body></html>");
    }


    @Transactional
    @PostMapping("/public/signin")
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

        //create a new cookie
        Cookie jwtCookie = new Cookie("jwt", encodeJWT);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(MAX_AGE);
        jwtCookie.setHttpOnly(true);//not allow javascript access

        Cookie userCookie = new Cookie("user", encodeUser);
        userCookie.setPath("/");
        userCookie.setMaxAge(MAX_AGE);

        Cookie roleCookie = new Cookie("role", role);
        roleCookie.setPath("/");
        roleCookie.setMaxAge(MAX_AGE);

        Cookie isLoggedIn = new Cookie("isLoggedIn", "true");
        isLoggedIn.setPath("/");
        isLoggedIn.setMaxAge(MAX_AGE);


        //add cookie to response
        response.addCookie(jwtCookie);
        response.addCookie(userCookie);
        response.addCookie(roleCookie);
        response.addCookie(isLoggedIn);


        return ResponseEntity.ok("Login success, JWT has been saved in cookie, role: " + role);
    }

    @Transactional
    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        //get cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
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
