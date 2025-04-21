package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.MyAPIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;


    private Long id = 1L;

    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new MyAPIException("No categories created till now");
        }
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());

        if(savedCategory!=null){
            throw new MyAPIException("Category with this name " + category.getCategoryName() + " already exists");
        }
        category.setCategoryId(id++);
        categoryRepository.save(category);
    }

    @Override
    public ResponseEntity<String> deleteCategory(Long categoryId) {
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId, "categoryId"));

        categoryRepository.delete(category);
        return new ResponseEntity<>("Category Deleted Successfully", HttpStatus.OK);
    }

    @Override
    public Category updateCategory(Category newCategory, Long categoryId) {

        Optional<Category> categories = categoryRepository.findById(categoryId);

        Category savedCategory = categories.orElseThrow(() ->  new ResourceNotFoundException("Category", categoryId, "categoryId"));


        savedCategory.setCategoryId(categoryId);
        savedCategory.setCategoryName(newCategory.getCategoryName());
        savedCategory = categoryRepository.save(savedCategory);
        return savedCategory;
    }
}
