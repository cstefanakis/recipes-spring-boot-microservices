package com.example.recipe_step_service.repositories;


import com.example.recipe_step_service.models.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Integer> {

    @Query("""
            SELECT rs FROM RecipeStep rs
            WHERE rs.recipeId = :recipeId
            """)
    List<RecipeStep> findRecipeStepsByRecipeId(@PathVariable("recipeId") Integer recipeId);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE RecipeStep rs
            SET rs.stepNumber = rs.stepNumber +1
            WHERE rs.recipeId = :recipeId
            AND rs.stepNumber >= :stepNumber
            """)
    void shiftStepsForward(
            @Param("stepNumber") Integer stepNumber,
            @Param("recipeId") Integer recipeId
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            DELETE FROM RecipeStep rs
            WHERE rs.recipeId = :recipeId
            """)
    void deleteAllByRecipeId(@Param("recipeId") Integer recipeId);
}
