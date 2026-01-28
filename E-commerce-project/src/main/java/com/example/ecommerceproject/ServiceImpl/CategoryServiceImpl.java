package com.example.ecommerceproject.ServiceImpl;

import com.example.ecommerceproject.Entity.Category;
import com.example.ecommerceproject.Exception.CategoryNotFoundException;
import com.example.ecommerceproject.Repository.CategoryRepo;
import com.example.ecommerceproject.Service.CategoryService;
import com.example.ecommerceproject.Service.FileStorageService;
import com.example.ecommerceproject.converter.StoreConverter;
import com.example.ecommerceproject.dto.ReadAbleCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private StoreConverter storeConverter;

    // ================= CREATE CATEGORY WITH DESCRIPTIONS =================
    @Override
    public ReadAbleCategory createCategory(Category category) {

        // --- LINK CATEGORY DESCRIPTIONS TO CATEGORY BEFORE SAVE ---
        if (category.getCategoryDescriptions() != null) {
            category.getCategoryDescriptions().forEach(desc -> desc.setCategory(category));
        }

        // --- SAVE CATEGORY ---
        Category savedCategory = categoryRepo.save(category);

        // --- CONVERT SAVED CATEGORY TO ReadAbleCategory ---
        return storeConverter.convertToReadable(savedCategory);
    }

    // ================= GET ALL CATEGORIES =================
    @Override
    public List<ReadAbleCategory> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();

        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("No categories found");
        }

        List<ReadAbleCategory> list = new ArrayList<>();
        for (Category c : categories) {
            list.add(storeConverter.convertToReadable(c));
        }

        return list;
    }

    // ================= GET CATEGORY BY ID =================
    @Override
    public ReadAbleCategory getCategoryById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));

        return storeConverter.convertToReadable(category);
    }

    // ================= UPDATE CATEGORY =================
    @Override
    public ReadAbleCategory updateCategory(Long id, Category categoryDetails) {
        Category existing = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));

        modelMapper.map(categoryDetails, existing);

        // --- LINK DESCRIPTIONS IF UPDATED ---
        if (existing.getCategoryDescriptions() != null) {
            existing.getCategoryDescriptions().forEach(desc -> desc.setCategory(existing));
        }

        Category updated = categoryRepo.save(existing);
        return  storeConverter.convertToReadable(updated);
    }

    // ================= DELETE CATEGORY =================
    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));

        categoryRepo.delete(category); // HARD DELETE
    }
    // Upload category image
    @Override
    public String uploadCategoryImage(Long categoryId, MultipartFile file) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        String directory = "categories"; // Folder under uploads
        String fileName = file.getOriginalFilename();

        // Upload file to disk
        fileStorageService.uploadFile(file, directory, fileName);

        // Save file name in category
        category.setCategoryImage(fileName);
        category.setUpdatedAt(new Date());
        categoryRepo.save(category);

        return "Category image uploaded successfully: " + fileName;
    }

    // Download category image
    @Override
    public Resource downloadCategoryImage(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        String fileName = category.getCategoryImage();
        if (fileName == null || fileName.isBlank()) {
            throw new RuntimeException("No image exists for this category");
        }

        String directory = "categories";
        return fileStorageService.downloadFile(directory, fileName);
    }

    // Delete category image
    @Override
    public String deleteCategoryImage(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        String fileName = category.getCategoryImage();
        if (fileName == null || fileName.isBlank()) {
            return "No image exists for this category";
        }

        String directory = "categories";
        boolean deleted = fileStorageService.deleteFile(directory, fileName);

        category.setCategoryImage(null);
        category.setUpdatedAt(new Date());
        categoryRepo.save(category);

        return deleted ? "Category image deleted successfully" :
                "Image file not found on server, but category record cleared";
    }

}


