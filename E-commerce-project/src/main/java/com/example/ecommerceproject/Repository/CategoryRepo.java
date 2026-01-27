package com.example.ecommerceproject.Repository;

import com.example.ecommerceproject.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    // Optional: sirf active categories ke liye
    List<Category> findByIsActiveTrue();
}
