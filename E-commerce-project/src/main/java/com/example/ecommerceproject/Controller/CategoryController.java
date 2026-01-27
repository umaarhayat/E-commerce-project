package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Entity.Category;
import com.example.ecommerceproject.Service.CategoryService;
import com.example.ecommerceproject.dto.ReadAbleCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // ========== Create Category ==========
    @PostMapping
    public ResponseEntity<ReadAbleCategory> createCategory(@RequestBody Category category) {
        ReadAbleCategory createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    // ========== Get All Categories ==========
    @GetMapping
    public ResponseEntity<List<ReadAbleCategory>> getAllCategories() {
        List<ReadAbleCategory> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // ========== Get Category By ID ==========
    @GetMapping("/{id}")
    public ResponseEntity<ReadAbleCategory> getCategoryById(@PathVariable Long id) {
        ReadAbleCategory category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    // ========== Update Category ==========
    @PutMapping("/{id}")
    public ResponseEntity<ReadAbleCategory> updateCategory(@PathVariable Long id,
                                                           @RequestBody Category category) {
        ReadAbleCategory updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    // ========== Delete Category ==========
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


    // Upload category image
    @PostMapping("/{categoryId}/image/upload")
    public ResponseEntity<String> uploadCategoryImage(
            @PathVariable Long categoryId,
            @RequestParam("file") MultipartFile file) {

        String message = categoryService.uploadCategoryImage(categoryId, file);
        return ResponseEntity.ok(message);
    }

    // Download category image
    @GetMapping("/{categoryId}/image/download")
    public ResponseEntity<Resource> downloadCategoryImage(@PathVariable Long categoryId) {
        Resource resource = categoryService.downloadCategoryImage(categoryId);

        String contentType = "application/octet-stream";
        try {
            contentType = java.nio.file.Files.probeContentType(java.nio.file.Paths.get(resource.getURI()));
        } catch (Exception ignored) {}

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // Delete category image
    @DeleteMapping("/{categoryId}/image/delete")
    public ResponseEntity<String> deleteCategoryImage(@PathVariable Long categoryId) {
        String message = categoryService.deleteCategoryImage(categoryId);
        return ResponseEntity.ok(message);
    }
}
