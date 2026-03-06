package com.example.ingredient_categories_service.repositories;


import com.example.ingredient_categories_service.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("""
            SELECT c.name FROM Category c
            WHERE c.id = :categoryId
            """)
    String findCategoryNameByCategoryId(@Param("categoryId")Integer categoryId);
}
