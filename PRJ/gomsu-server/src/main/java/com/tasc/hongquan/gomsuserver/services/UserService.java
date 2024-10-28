package com.tasc.hongquan.gomsuserver.services;

import com.tasc.hongquan.gomsuserver.models.Role;
import com.tasc.hongquan.gomsuserver.models.User;
import com.tasc.hongquan.gomsuserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(int id){
        return userRepository.findById(id).orElse(null);
    }

    public User createAccount(User user, int role_id){
        Role role = new Role();
        role.setId(role_id); //user
        user.setRole(role);
        return userRepository.save(user);
    }



    private User updateUser(User user){
        return userRepository.save(user);
    }


}
