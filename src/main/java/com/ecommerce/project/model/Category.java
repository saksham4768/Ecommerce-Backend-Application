package com.ecommerce.project.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private Long categoryId;

    @NotBlank(message = "Category name can not be empty")
    @Size(min = 5, message = "Category must contain at least 5 characters")
    private String categoryName;
}
