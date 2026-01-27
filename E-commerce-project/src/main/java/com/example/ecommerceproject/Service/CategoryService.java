package com.example.ecommerceproject.Service;

import com.example.ecommerceproject.Entity.Category;
import com.example.ecommerceproject.dto.ReadAbleCategory;
import com.example.ecommerceproject.dto.ReadAbleCategoryDescription;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    ReadAbleCategory createCategory(Category category);

    List<ReadAbleCategory> getAllCategories();

    ReadAbleCategory getCategoryById(Long id);

    ReadAbleCategory updateCategory(Long id, Category categoryDetails);

    void deleteCategory(Long id);


    // IMAGE CRUD
    String uploadCategoryImage(Long categoryId, MultipartFile image);

    Resource downloadCategoryImage(Long categoryId);
    String deleteCategoryImage(Long categoryId);

}
