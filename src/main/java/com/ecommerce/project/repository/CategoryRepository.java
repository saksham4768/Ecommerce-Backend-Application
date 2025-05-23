package com.ecommerce.project.repository;

import com.ecommerce.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM categories c WHERE c.categoryName = :categoryName OR c.categoryId = :categoryId")
    Category findByCategoryNameAndId(@Param("categoryName") String categoryName,
                                     @Param("categoryId") Long categoryId);

}
