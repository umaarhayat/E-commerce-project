package com.example.ecommerceproject.Service;

import com.example.ecommerceproject.Entity.Category;
import com.example.ecommerceproject.dto.CategoryDto;
import com.example.ecommerceproject.dto.ReadAbleCategory;

import java.util.List;

public interface CategoryService {

    ReadAbleCategory createCategory(Category category);

    List<ReadAbleCategory> getAllCategories();

    ReadAbleCategory getCategoryById(Long id);

    ReadAbleCategory updateCategory(Long id, Category categoryDetails);

    void deleteCategory(Long id);
}
