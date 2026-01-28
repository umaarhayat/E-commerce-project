package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Entity.Category;
import com.example.ecommerceproject.Service.CategoryService;
import com.example.ecommerceproject.Service.ProductService;
import com.example.ecommerceproject.dto.GenericResponse;
import com.example.ecommerceproject.dto.ReadAbleCategory;
import com.example.ecommerceproject.dto.ReadAbleProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    // ========== Create Category ==========
    @PostMapping
    public GenericResponse createCategory(@RequestBody Category category) {
        return GenericResponse.success(
                categoryService.createCategory(category),
                "Category created successfully"
        );
    }

    // ========== Get All Categories ==========
    @GetMapping
    public GenericResponse getAllCategories() {
        return GenericResponse.success(
                categoryService.getAllCategories(),
                "Categories fetched successfully"
        );
    }

    // ========== Get Category By ID ==========
    @GetMapping("/{id}")
    public GenericResponse getCategoryById(@PathVariable Long id) {
        return GenericResponse.success(
                categoryService.getCategoryById(id),
                "Category fetched successfully"
        );
    }

    // ========== Update Category ==========
    @PutMapping("/{id}")
    public GenericResponse updateCategory(
            @PathVariable Long id,
            @RequestBody Category category
    ) {
        return GenericResponse.success(
                categoryService.updateCategory(id, category),
                "Category updated successfully"
        );
    }

    // ========== Delete Category ==========
    @DeleteMapping("/{id}")
    public GenericResponse deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return GenericResponse.success(
                null,
                "Category deleted successfully"
        );
    }

    // ========== Upload Category Image ==========
    @PostMapping("/{categoryId}/image/upload")
    public GenericResponse uploadCategoryImage(
            @PathVariable Long categoryId,
            @RequestParam("file") MultipartFile file
    ) {
        String imageName = categoryService.uploadCategoryImage(categoryId, file);

        return GenericResponse.success(
                imageName,
                "Category image uploaded successfully"
        );
    }


    // ========== Download Category Image ==========
    // ⚠ File download always ResponseEntity<Resource>
    @GetMapping("/{categoryId}/image/download")
    public ResponseEntity<Resource> downloadCategoryImage(
            @PathVariable Long categoryId
    ) {
        Resource resource = categoryService.downloadCategoryImage(categoryId);

        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(Paths.get(resource.getURI()));
        } catch (Exception ignored) {}

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }

    // ========== Delete Category Image ==========
    @DeleteMapping("/{categoryId}/image/delete")
    public GenericResponse deleteCategoryImage(
            @PathVariable Long categoryId
    ) {
        String deletedImageName = categoryService.deleteCategoryImage(categoryId);
        return GenericResponse.success(
                deletedImageName,
                "Category image deleted successfully"
        );
    }
/*
  # Fetch all products for a selected category using categoryId
 */

    @GetMapping("/{categoryId}/products")
    public GenericResponse<List<ReadAbleProduct>> getProductsByCategory(
            @PathVariable Long categoryId) {

        List<ReadAbleProduct> products =
                productService.getProductsByCategoryId(categoryId);

        return GenericResponse.success(
                products,
                "Products fetched successfully for selected category"
        );
    }
}
