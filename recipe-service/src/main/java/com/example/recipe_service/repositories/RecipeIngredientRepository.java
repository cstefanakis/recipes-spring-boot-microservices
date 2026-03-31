package com.example.recipe_service.repositories;

import com.example.recipe_service.models.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {

    @Query("""
            SELECT ri FROM RecipeIngredient ri
            JOIN ri.recipe r
            WHERE r.id = :recipeId
            """)
    List<RecipeIngredient> findRecipeIngredientsByRecipeId(@Param("recipeId") Integer recipeId);

    @Query("""
            SELECT COUNT(ri) > 0 FROM RecipeIngredient ri
            WHERE ri.ingredientId = :ingredientId
            """)
    boolean existsWithIngredientId(@Param("ingredientId")Integer ingredientId);
}
