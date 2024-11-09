package com.tasc.hongquan.gomsuserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasc.hongquan.gomsuserver.DTO.ForgotResponse;
import com.tasc.hongquan.gomsuserver.DTO.LoginDTO;
import com.tasc.hongquan.gomsuserver.DTO.RegisterDTO;
import com.tasc.hongquan.gomsuserver.Jwt.JwtTokenProvider;
import com.tasc.hongquan.gomsuserver.models.CustomUserDetails;
import com.tasc.hongquan.gomsuserver.models.Token;
import com.tasc.hongquan.gomsuserver.models.User;
import com.tasc.hongquan.gomsuserver.services.*;
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
import org.springframework.security.core.GrantedAuthority;
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
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    public static final int MAX_AGE = 3600 * 24;// 1 day
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, AuthenticationManager authen, JwtTokenProvider jwt, ObjectMapper objectMapper, TokenServiceImpl tokenServiceImpl, EmailServiceImpl emailServiceImpl) {
        this.userService = userServiceImpl;
        this.authen = authen;
        this.jwtTokenProvider = jwt;
        this.objectMapper = objectMapper;
        this.tokenService = tokenServiceImpl;
        this.emailService = emailServiceImpl;
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

    @GetMapping("/public/forgot/find-account/{email}")
    public ResponseEntity<Object> findUser(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            ForgotResponse response = ForgotResponse.builder()
                    .userId(user.getUserId())
                    .name(user.getFullName())
                    .phoneNumber(user.getPhoneNumber())
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error finding user: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/public/request-change-password")
    public ResponseEntity<String> requestChangePassword(@RequestParam String email) {
        try {
            //find user
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            //generate token otp
            int otp = tokenService.generateOTP();


            Token token = new Token().builder()
                    .token(otp)
                    .tokenType("otp")
                    .user(user)
                    .build();

            //check otp if exists and not revoked with user
            while (!tokenService.checkValidOTP(otp, user.getUserId())) {
                //false
                //set revoked old token
                try {
                    tokenService.revokeToken(otp, user.getUserId());

                } catch (Exception e) {
                    logger.error("Error revoking token: " + e.getMessage());
                }
                otp = tokenService.generateOTP();

            }
            tokenService.saveToken(token);

            //send email
            emailService.sendEmail(user.getEmail(), "Change password", "Your OTP is: " + otp);

            return ResponseEntity.ok("OTP has been sent to your email");
        } catch (Exception e) {
            logger.error("Error requesting change password: " + e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/public/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String userId, @RequestParam String otpRequest, @RequestParam String newPassword) {

        try {
            int otp = Integer.parseInt(otpRequest);
            if (!tokenService.validateToken(otp, userId)) {
                logger.info("OTP is valid");
                //changepass
                User user = userService.changePass(userId, newPassword);
                //revoke token
                tokenService.revokeToken(otp, userId);
                return ResponseEntity.ok("Password changed successfully");
            }
            return ResponseEntity.badRequest().body("OTP is invalid");
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
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
        String email;
        String role;
        try {
            Authentication authentication = authen.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            email = String.valueOf(authentication.getName());
            GrantedAuthority authority = authentication.getAuthorities().stream().iterator().next();
            role = authority.getAuthority();

        } catch (AuthenticationException authenticationException) {
            throw new Exception("Invalid username or password", authenticationException);
        }
        final CustomUserDetails userDetails = userService.loadUserByUsername(loginDTO.getEmail());
        final String jwt = jwtTokenProvider.generateToken(userDetails);

//        final String email = String.valueOf(jwtTokenProvider.getEmailFromJWT(jwt));
//        final String role = userService.getRoleName(email);

        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

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
