package com.example.ecom_application.service;

import com.example.ecom_application.dtos.OrderResponse;

import java.util.Optional;

public interface OrderService {
    Optional<OrderResponse> createOrder(String userId);
}
