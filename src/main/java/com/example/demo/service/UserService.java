package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(String customerId, String token, String endpointArn) {
        User user = new User();
        user.setCustomerId(customerId);
        user.setMobileToken(token);
        user.setEndpointArn(endpointArn);
        return userRepository.save(user);
    }
}