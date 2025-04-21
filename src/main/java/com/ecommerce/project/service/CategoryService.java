package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    public List<Category> getAllCategories();
    void createCategory(Category category);

    ResponseEntity<String> deleteCategory(Long categoryId);

    Category updateCategory(Category category, Long categoryId);
}
