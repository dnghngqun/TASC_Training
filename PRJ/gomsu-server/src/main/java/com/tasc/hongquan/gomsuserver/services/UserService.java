package com.tasc.hongquan.gomsuserver.services;


import com.tasc.hongquan.gomsuserver.DTO.RegisterDTO;
import com.tasc.hongquan.gomsuserver.exception.CustomException;
import com.tasc.hongquan.gomsuserver.models.CustomUserDetails;
import com.tasc.hongquan.gomsuserver.models.Role;
import com.tasc.hongquan.gomsuserver.models.User;
import com.tasc.hongquan.gomsuserver.repositories.RoleRepository;
import com.tasc.hongquan.gomsuserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUserId(String id) {
        return userRepository.findByUserId(id).get();
    }

    public User createAccountWithGG(User user, int roleId) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already in use.");
        }

        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());


        // Retrieve the role from the database
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found."));

        user.setRole(role);

        return userRepository.save(user);
    }

    public User createAccount(RegisterDTO userDto, int roleId) {
        // Check if the user already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        if (userRepository.existsByPhoneNumber(userDto.getPhone_number())) {
            throw new IllegalArgumentException("Phone number already in use.");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhone_number());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());


        // Retrieve the role from the database
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found."));

        user.setRole(role);

        return userRepository.save(user);
    }


    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public String getRoleName(String email) throws CustomException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get().getRole().getRoleName();
        } else {
            throw new CustomException("User not found");
        }

    }

    public User changePass(String userId, String password) {
        User user = userRepository.findByUserId(userId).get();
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = Optional.of(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email)));

        // Ở đây ánh xạ User vào đối tượng UserDetails của Spring Security
        // same as method findUserByEmail
        return new CustomUserDetails(userOptional.get());
    }


}
