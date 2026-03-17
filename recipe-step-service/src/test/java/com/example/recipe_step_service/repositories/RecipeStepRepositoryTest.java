package com.example.recipe_step_service.repositories;

import com.example.recipe_step_service.models.RecipeStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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
        //Arrest
        Integer recipeId = 1;
        //Act
        List<RecipeStep> result = recipeStepRepository.findRecipeStepsByRecipeId(recipeId);
        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(recipeStep01));
        assertTrue(result.contains(recipeStep02));
    }
}