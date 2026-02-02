package com.example.ecommerceproject.specification.impl;

import com.example.ecommerceproject.Entity.Product;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProductSpecificationImpl implements ProductSpecification {

    @Override
    public Specification<Product> searchProduct(
            String storeCode,
            String storeName,
            Long productId,
            String productName,
            String categoryName,
            Long categoryId
    ) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {


            Join<Product, ?> categoryJoin = root.join("category", JoinType.LEFT);
            Join<Product, ?> storeJoin = root.join("merchantStore", JoinType.LEFT);

            Predicate predicate = cb.conjunction(); // start with TRUE

            // ------------- Filters ---------------


            if (storeCode != null && !storeCode.isEmpty()) {
                predicate = cb.and(
                        predicate,
                        cb.equal(storeJoin.get("storeCode"), storeCode)
                );
            }

            if (storeName != null && !storeName.isEmpty()) {
                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(storeJoin.get("storeName")),
                                "%" + storeName.toLowerCase() + "%"
                        )
                );
            }

            if (productId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("id"), productId));
            }

            if (productName != null && !productName.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("productName")), "%" + productName.toLowerCase() + "%"));
            }

            if (categoryName != null && !categoryName.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(categoryJoin.get("categoryName")), "%" + categoryName.toLowerCase() + "%"));
            }

            if (categoryId != null) {
                predicate = cb.and(predicate,
                        cb.equal(categoryJoin.get("id"), categoryId));
            }

            return predicate;
        };
    }
}
