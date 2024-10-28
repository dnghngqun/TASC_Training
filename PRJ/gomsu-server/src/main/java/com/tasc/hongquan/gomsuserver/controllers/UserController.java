package com.tasc.hongquan.gomsuserver.controllers;

import com.tasc.hongquan.gomsuserver.models.User;
import com.tasc.hongquan.gomsuserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/register")
    public ResponseEntity<User> createUser(User user) {
        return ResponseEntity.ok(userService.createAccount(user, 3));
    }

    @RequestMapping("/shipper/register")
    public ResponseEntity<User> createShipper(User user) {
        return ResponseEntity.ok(userService.createAccount(user, 2));
    }


}
