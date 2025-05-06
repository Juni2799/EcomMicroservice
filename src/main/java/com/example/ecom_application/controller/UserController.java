package com.example.ecom_application.controller;

import com.example.ecom_application.dtos.UserRequest;
import com.example.ecom_application.dtos.UserResponse;
import com.example.ecom_application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers(){
        return new ResponseEntity<>(userService.listUsers(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId){
        return userService.listUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createNewUser(@RequestBody UserRequest userRequest){
        userService.createNewUser(userRequest);
        return ResponseEntity.ok("User added successfully!!!");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateExistingUser(@PathVariable Long userId,
                                                     @RequestBody UserRequest userRequest){
        boolean updated = userService.updateExistingUser(userId, userRequest);
        if(updated){
            return ResponseEntity.ok("User updated successfully!!!");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId){
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
