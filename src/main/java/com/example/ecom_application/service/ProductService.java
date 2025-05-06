package com.example.ecom_application.service;

import com.example.ecom_application.dtos.ProductRequest;
import com.example.ecom_application.dtos.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductResponse> listProducts();
    Optional<ProductResponse> getProductById(Long productId);
    ProductResponse createNewProduct(ProductRequest productRequest);
    ProductResponse updateExistingProduct(Long productId, ProductRequest productRequest);

    void deleteProductById(Long productId);

    List<ProductResponse> searchProducts(String keyword);
}
