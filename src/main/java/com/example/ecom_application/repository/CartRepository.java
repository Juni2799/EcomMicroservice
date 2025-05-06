package com.example.ecom_application.repository;

import com.example.ecom_application.entity.Cart;
import com.example.ecom_application.entity.Product;
import com.example.ecom_application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<Cart> findByUser(User user);

    void deleteByUser(User user);
}
