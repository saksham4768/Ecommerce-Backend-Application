package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    public final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId){
        ProductDTO savedProductDTO = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProduct(@RequestParam(value = "pageNumber", defaultValue =  AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                         @RequestParam(value = "pageSize", defaultValue =  AppConstants.PAGE_SIZE) Integer pageSize,
                                                         @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
                                                         @RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder){
        ProductResponse productResponse = productService.getAllProduct(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ProductResponse getProductByCategory(@PathVariable Long categoryId,
                                                @RequestParam(value = "pageNumber", defaultValue =  AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                @RequestParam(value = "pageSize", defaultValue =  AppConstants.PAGE_SIZE) Integer pageSize,
                                                @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY) String sortBy,
                                                @RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder){
        return productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(value = "pageNumber", defaultValue =  AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue =  AppConstants.PAGE_SIZE) Integer pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
                                                                @RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder){
        ProductResponse productResponse = productService.searchProductsByKeyword("%" + keyword + "%",pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long productId){
        ProductDTO updatedProductDTO= productService.updateProductById(productDTO, productId);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO productDTO = productService.deleteProductById(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam MultipartFile Image) throws IOException {
        ProductDTO updatedProductDTO = productService.updateProductImage(productId, Image);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }
}
