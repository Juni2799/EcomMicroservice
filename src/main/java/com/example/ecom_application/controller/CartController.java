package com.example.ecom_application.controller;

import com.example.ecom_application.dtos.CartItemRequest;
import com.example.ecom_application.entity.Cart;
import com.example.ecom_application.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId
                                         , @RequestBody CartItemRequest request){
        if(cartService.addToCart(userId, request)) return ResponseEntity.status(HttpStatus.CREATED).build();

        return ResponseEntity.badRequest().body("Product out of stock or User/Product not found!!!");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") String userId,
                                               @PathVariable Long productId){
        boolean deleted = cartService.deleteItemFromCart(userId, productId);

        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getCart(@RequestHeader("X-User-ID") String userId){
        List<Cart> carts = cartService.getCartByUser(userId);
        return carts != null ? ResponseEntity.ok(carts) : ResponseEntity.notFound().build();
    }
}
