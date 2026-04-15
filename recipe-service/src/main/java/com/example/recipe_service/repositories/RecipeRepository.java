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
    Page<Recipe> findRecipesByCategoryId(@Param("categoryId") Integer categoryId,
                                        Pageable pageable);

    @Query("""
            SELECT COUNT(r) > 0 FROM Recipe r
            WHERE LOWER(r.title) = LOWER(:title)
            """)
    boolean titleExists(@Param("title") String title);

    @Query("""
            SELECT r.userId FROM Recipe r
            WHERE r.id = :recipeId
            """)
    Integer findRecipeOwnerIdByRecipeId(@Param("recipeId") Integer recipeId);
}
