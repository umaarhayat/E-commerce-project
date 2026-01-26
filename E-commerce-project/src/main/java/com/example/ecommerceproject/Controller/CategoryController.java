package com.example.ecommerceproject.Controller;
import com.example.ecommerceproject.Entity.Category;
import com.example.ecommerceproject.Service.CategoryService;
import com.example.ecommerceproject.dto.ReadAbleCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/categories")
    public class CategoryController {

        @Autowired
        private CategoryService categoryService;

        @PostMapping
        public ResponseEntity<ReadAbleCategory> createCategory(@RequestBody Category category) {
            return ResponseEntity.ok(categoryService.createCategory(category));
        }

        @GetMapping
        public ResponseEntity<List<ReadAbleCategory>> getAllCategories() {
            return ResponseEntity.ok(categoryService.getAllCategories());
        }

        @GetMapping("/{id}")
        public ResponseEntity<ReadAbleCategory> getCategoryById(@PathVariable Long id) {
            return ResponseEntity.ok(categoryService.getCategoryById(id));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ReadAbleCategory> updateCategory(@PathVariable Long id, @RequestBody Category category) {
            return ResponseEntity.ok(categoryService.updateCategory(id, category));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        }


    }





