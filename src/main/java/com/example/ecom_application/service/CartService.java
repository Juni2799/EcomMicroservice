package com.example.ecom_application.service;

import com.example.ecom_application.dtos.CartItemRequest;
import com.example.ecom_application.entity.Cart;

import java.util.List;

public interface CartService {
    boolean addToCart(String userId, CartItemRequest request);

    boolean deleteItemFromCart(String userId, Long productId);

    List<Cart> getCartByUser(String userId);

    void clearCart(String userId);
}
