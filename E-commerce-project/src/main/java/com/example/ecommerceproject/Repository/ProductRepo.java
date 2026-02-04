package com.example.ecommerceproject.Repository;

import com.example.ecommerceproject.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // Fetch products by category ID
    List<Product> findByCategoryId(Long categoryId);

    // Pagination support
    Page<Product> findAll(Pageable pageable);

}
