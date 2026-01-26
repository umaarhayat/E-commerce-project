package com.example.ecommerceproject.Repository;

import com.example.ecommerceproject.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    List<Product> findByIsDeleteFalse();
    Optional<Product> findByIdAndIsDeleteFalse(Long id);
    List<Product> findByMerchantStoreIdAndIsDeleteFalse(Long merchantStoreId);
    List<Product> findByCategoryIdAndIsDeleteFalse(Long categoryId);
}
