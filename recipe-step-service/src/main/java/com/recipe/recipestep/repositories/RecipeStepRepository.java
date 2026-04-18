package com.recipe.recipestep.repositories;


import com.recipe.recipestep.models.RecipeStep;
import jakarta.transaction.Transactional;
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

    @Transactional
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

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE RecipeStep rs
            SET rs.stepNumber = rs.stepNumber -1
            WHERE rs.recipeId = :recipeId
            AND rs.stepNumber >= :stepNumber
            """)
    void shiftStepsBackward(
            @Param("stepNumber") Integer stepNumber,
            @Param("recipeId") Integer recipeId
    );


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            DELETE FROM RecipeStep rs
            WHERE rs.recipeId = :recipeId
            """)
    void deleteAllByRecipeId(@Param("recipeId") Integer recipeId);

    @Query("""
            SELECT MAX(rs.stepNumber)
            FROM RecipeStep rs
            WHERE rs.recipeId = :recipeId
            """)
    Integer findBiggerRecipeNumberByRecipeId(Integer recipeId);

    @Query("""
            SELECT rs.id FROM RecipeStep rs
            WHERE rs.recipeId = :recipeId
            AND rs.stepNumber = :stepNumber
            """)
    Integer findRecipeStepIdByRecipeIdAndStepNumber(@Param("recipeId") Integer recipeId,
                                                    @Param("stepNumber") Integer stepNumber);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE RecipeStep rs
            SET rs.stepNumber = :stepNumber
            WHERE rs.id = :recipeStepId
            """)
    void updateRecipeStepStepNumber(@Param("recipeStepId") Integer recipeStepId,
                                    @Param("stepNumber") Integer stepNumber);

    @Query("""
            SELECT rs.stepNumber FROM RecipeStep rs
            WHERE rs.id = :recipeStepId
            """)
    Integer findRecipeStepByRecipeStepId(@Param("recipeStepId") Integer recipeStepId);

    @Query("""
            SELECT rs.recipeId FROM RecipeStep rs
            WHERE rs.id = :recipeStepId
            """)
    Integer findRecipeIdByRecipeStepId(Integer recipeStepId);
}
