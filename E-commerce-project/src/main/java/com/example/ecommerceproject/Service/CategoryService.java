package com.example.ecommerceproject.Service;

import com.example.ecommerceproject.Entity.Category;
import com.example.ecommerceproject.dto.PageResponse;
import com.example.ecommerceproject.dto.ReadAbleCategory;
import com.example.ecommerceproject.dto.ReadAbleCategoryDescription;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    ReadAbleCategory createCategory(Category category);

    // ================= GET ALL CATEGORIES =================
    PageResponse<ReadAbleCategory> getAllCategories(int pageNumber, int pageSize);

    ReadAbleCategory getCategoryById(Long id);

    ReadAbleCategory updateCategory(Long id, Category categoryDetails);

    void deleteCategory(Long id);


    // IMAGE CRUD
    String uploadCategoryImage(Long categoryId, MultipartFile image);

    Resource downloadCategoryImage(Long categoryId);
    String deleteCategoryImage(Long categoryId);

}
