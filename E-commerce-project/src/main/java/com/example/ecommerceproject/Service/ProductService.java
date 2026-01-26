package com.example.ecommerceproject.Service;

import com.example.ecommerceproject.Entity.Product;
import com.example.ecommerceproject.dto.ReadAbleProduct;

import java.util.List;

public interface ProductService {

    // Create a new product
    ReadAbleProduct createProduct(Product product);

    // Get all products (only non-deleted)
    List<ReadAbleProduct> getAllProducts();

    // Get a product by its ID
    ReadAbleProduct getProductById(Long id);

    // Update a product by its ID
    ReadAbleProduct updateProduct(Long id, Product productDetails);

    // Soft delete a product by its ID (isDelete = true)
    void deleteProduct(Long id);
}
