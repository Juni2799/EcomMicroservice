package com.example.ecom_application.service;

import com.example.ecom_application.dtos.CartItemRequest;
import com.example.ecom_application.entity.Cart;
import com.example.ecom_application.entity.Product;
import com.example.ecom_application.entity.User;
import com.example.ecom_application.repository.CartRepository;
import com.example.ecom_application.repository.ProductRepository;
import com.example.ecom_application.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//@Transactional
public class CartServiceImpl implements CartService{
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public boolean addToCart(String userId, CartItemRequest request) {
        // look for product
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if(productOpt.isEmpty()){
            return false;
        }

        Product product = productOpt.get();
        if(product.getStockQuantity() < request.getQuantity()) return false;

        // look for user
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()) return false;

        User user = userOpt.get();

        Cart existingCart = cartRepository.findByUserAndProduct(user, product);
        if(existingCart != null){
            // perform update of existing cart
            existingCart.setQuantity(existingCart.getQuantity() + request.getQuantity());
            existingCart.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCart.getQuantity())));
            cartRepository.save(existingCart);
        }else{
            // Create new cart item with requested product
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(request.getQuantity());
            cart.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartRepository.save(cart);
        }

        return true;
    }

    @Transactional
    @Override
    public boolean deleteItemFromCart(String userId, Long productId) {
        // look for product
        Optional<Product> productOpt = productRepository.findById(productId);
        // look for user
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        if(productOpt.isPresent() && userOpt.isPresent()){
            cartRepository.deleteByUserAndProduct(userOpt.get(), productOpt.get());
            return true;
        }

        return false;
    }

    @Override
    public List<Cart> getCartByUser(String userId) {
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        return userOpt.map(cartRepository::findByUser).orElse(null);
//        if(userOpt.isEmpty()){
//            return null;
//        }
//        return cartRepository.findByUser(userOpt.get());
    }

    @Transactional
    @Override
    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId))
                .ifPresent(cartRepository::deleteByUser);
    }
}
