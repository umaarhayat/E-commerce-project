package com.example.ecommerceproject.specification.impl;

import com.example.ecommerceproject.Entity.Product;
import org.springframework.data.jpa.domain.Specification;

public interface ProductSpecification {
    Specification<Product> searchProduct(
            String storeCode,
            String storeName,
            Long productId,
            String productName,
            String categoryName,
            Long categoryId
    );
}
