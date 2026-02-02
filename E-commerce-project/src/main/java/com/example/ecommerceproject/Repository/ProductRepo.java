package com.example.ecommerceproject.Repository;

import com.example.ecommerceproject.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // Add custom queries if needed
    List<Product> findByCategoryId(Long categoryId);


}
