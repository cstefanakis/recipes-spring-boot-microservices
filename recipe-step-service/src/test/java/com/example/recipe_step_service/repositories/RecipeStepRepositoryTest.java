package com.example.recipe_step_service.repositories;

import com.example.recipe_step_service.models.RecipeStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RecipeStepRepositoryTest {

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    private RecipeStep recipeStep01;
    private RecipeStep recipeStep02;

    @BeforeEach
    void setup(){
        this.recipeStep01 = recipeStepRepository.save(RecipeStep.builder()
                        .recipeId(1)
                        .description("description")
                        .imgUrl("url")
                        .stepNumber(1)
                .build());

        this.recipeStep02 = recipeStepRepository.save(RecipeStep.builder()
                .recipeId(1)
                .description("description")
                .imgUrl("url")
                .stepNumber(2)
                .build());
    }

    @Test
    void findRecipeStepsByRecipeId() {
        //Arrange
        Integer recipeId = 1;
        //Act
        List<RecipeStep> result = recipeStepRepository.findRecipeStepsByRecipeId(recipeId);
        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(recipeStep01));
        assertTrue(result.contains(recipeStep02));
    }

    @Test
    void shiftStepsForward() {
        //Arrange
        Integer recipeId = 1;
        Integer stepNumber = 2;
        //Act
        recipeStepRepository.shiftStepsForward(stepNumber, recipeId);
        Optional<RecipeStep> recipeStep = recipeStepRepository.findById(this.recipeStep02.getId());
        //Assert
        assertTrue(recipeStep.isPresent());
        assertEquals(3, recipeStep.get().getStepNumber());
    }

    @Test
    void deleteAllByRecipeId() {
        //Arrange
        Integer recipeId = 1;
        //Act
        recipeStepRepository.deleteAllByRecipeId(recipeId);
        Optional<RecipeStep> recipeStep01 = recipeStepRepository.findById(this.recipeStep01.getId());
        Optional<RecipeStep> recipeStep02 = recipeStepRepository.findById(this.recipeStep02.getId());
        //Assert
        assertFalse(recipeStep01.isPresent());
        assertFalse(recipeStep02.isPresent());
    }
}