package com.tasc.hongquan.gomsuserver.services;

import com.tasc.hongquan.gomsuserver.DTO.RegisterDTO;
import com.tasc.hongquan.gomsuserver.exception.CustomException;
import com.tasc.hongquan.gomsuserver.models.CustomUserDetails;
import com.tasc.hongquan.gomsuserver.models.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserByUserId(String id);

    User createAccountWithGG(User user, int roleId);

    User createAccount(RegisterDTO userDto, int roleId);

    User updateUser(User user);

    User getUserById(String id);

    User getUserByEmail(String email);

    String getRoleName(String email) throws CustomException;

    User changePass(String userId, String password);

    CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
