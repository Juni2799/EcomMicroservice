package com.example.ecom_application.service;

import com.example.ecom_application.dtos.UserRequest;
import com.example.ecom_application.dtos.UserResponse;
import com.example.ecom_application.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponse> listUsers();
    Optional<UserResponse> listUserById(Long userId);
    void createNewUser(UserRequest userRequest);
    boolean updateExistingUser(Long userId, UserRequest userRequest);
    void deleteUserById(Long userId);
}
