package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.MyAPIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper){
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new MyAPIException("No categories created till now");
        }

        //convert the all object category to category DTO
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category categoryFromDB = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());

        if(categoryFromDB!=null) {
            throw new MyAPIException("Category with this name " + categoryDTO.getCategoryName() + " already exists");
        }

        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public ResponseEntity<CategoryDTO> deleteCategory(Long categoryId) {
        List<Category> categories = categoryRepository.findAll();

        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId, "categoryId"));

        categoryRepository.delete(category);
        CategoryDTO deletedCategoryDTO = modelMapper.map(category, CategoryDTO.class);
        return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO newCategoryDTO, Long categoryId) {

        Optional<Category> categories = categoryRepository.findById(categoryId);

        Category savedCategory = categories.orElseThrow(() ->  new ResourceNotFoundException("Category", categoryId, "categoryId"));


        savedCategory.setCategoryId(categoryId);
        savedCategory.setCategoryName(newCategoryDTO.getCategoryName());
        savedCategory = categoryRepository.save(savedCategory);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
