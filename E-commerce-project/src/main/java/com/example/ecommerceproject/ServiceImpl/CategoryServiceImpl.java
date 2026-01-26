package com.example.ecommerceproject.ServiceImpl;

import com.example.ecommerceproject.Entity.Category;
import com.example.ecommerceproject.Exception.CategoryNotFoundException;
import com.example.ecommerceproject.Repository.CategoryRepo;
import com.example.ecommerceproject.Service.CategoryService;
import com.example.ecommerceproject.dto.ReadAbleCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ReadAbleCategory createCategory(Category category) {
        return modelMapper.map(categoryRepo.save(category), ReadAbleCategory.class);
    }

    @Override
    public List<ReadAbleCategory> getAllCategories() {
        List<Category> categories = categoryRepo.findByIsActiveTrue();
        if (categories.isEmpty()) throw new CategoryNotFoundException("No categories found");

        List<ReadAbleCategory> list = new ArrayList<>();
        for (Category c : categories) list.add(modelMapper.map(c, ReadAbleCategory.class));
        return list;
    }

    @Override
    public ReadAbleCategory getCategoryById(Long id) {
        Category category = categoryRepo.findByIdAndIsActiveTrue(id);
        if (category == null) throw new CategoryNotFoundException("Category not found with ID: " + id);
        return modelMapper.map(category, ReadAbleCategory.class);
    }

    @Override
    public ReadAbleCategory updateCategory(Long id, Category categoryDetails) {
        Category existing = categoryRepo.findByIdAndIsActiveTrue(id);
        if (existing == null) throw new CategoryNotFoundException("Category not found with ID: " + id);
        modelMapper.map(categoryDetails, existing);
        return modelMapper.map(categoryRepo.save(existing), ReadAbleCategory.class);
    }

    @Override
    public void deleteCategory(Long id) {
        Category existing = categoryRepo.findByIdAndIsActiveTrue(id);
        if (existing == null) throw new CategoryNotFoundException("Category not found with ID: " + id);
        existing.setActive(false);
        categoryRepo.save(existing);
    }
}
