package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api")
public class CategoryController {

    //final in constructor injection is a best practice in Java because it enforces immutability and ensures that the dependency is initialized exactly once.
    //Why Object declare of interface -> Loose Coupling
    private final CategoryService categoryService;



    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/echo")
    public ResponseEntity<String> getEchoMessage(@RequestParam(name = "message") String message){
        return new ResponseEntity<>("Echoed Message " + message, HttpStatus.OK);
    }
    @GetMapping("/public/categories")
    //@RequestMapping(value = "/api/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBY,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        CategoryResponse categoryResponse =  categoryService.getAllCategories(pageNumber, pageSize, sortBY, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody  CategoryDTO categoryDTO){
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable  Long categoryId){
            return categoryService.deleteCategory(categoryId);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){
            CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
            return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }
}
