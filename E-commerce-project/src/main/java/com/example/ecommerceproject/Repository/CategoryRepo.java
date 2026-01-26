package com.example.ecommerceproject.Repository;

import com.example.ecommerceproject.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    List<Category> findByIsActiveTrue();
    Category findByIdAndIsActiveTrue(Long id);
}
