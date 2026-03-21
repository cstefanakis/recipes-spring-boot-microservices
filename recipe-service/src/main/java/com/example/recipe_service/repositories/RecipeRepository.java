package com.example.recipe_service.repositories;

import com.example.recipe_service.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    @Query("""
            SELECT r FROM Recipe r
            JOIN r.categories c
            WHERE c.id = :categoryId
            """)
    Page<Recipe> findRecipeByCategoryId(@Param("categoryId") Integer categoryId,
                                        Pageable pageable);
}
