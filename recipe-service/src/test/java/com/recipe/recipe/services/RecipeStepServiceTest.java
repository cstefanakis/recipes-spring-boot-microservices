package com.recipe.recipe.services;

import com.recipe.recipe.clients.RecipeStepClient;
import com.recipe.recipe.dtos.recipeStep.RecipeStepResponseDto;
import com.recipe.recipe.models.Category;
import com.recipe.recipe.models.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RecipeStepServiceTest {

    @InjectMocks
    private RecipeStepService recipeStepService;

    @Mock
    private RecipeStepClient recipeStepClient;

    private Recipe recipe;
    private RecipeStepResponseDto recipeStep;


    @BeforeEach
    void setup(){
        Category category = Category.builder()
                .id(1)
                .name("Category")
                .imgUrl("url")
                .build();

        this.recipe = Recipe.builder()
                .id(1)
                .title("title")
                .description("description")
                .imgUrl("url")
                .categories(List.of(category))
                .userId(2)
                .build();

        this.recipeStep = RecipeStepResponseDto.builder()
                .id(1)
                .recipeId(1)
                .stepNumber(1)
                .imgUrl("recipe step url")
                .description("recipe step description")
                .build();
    }

    @Test
    void getRecipeStepsByRecipeId() {
        //Arrest
        Integer recipeId = this.recipe.getId();
        List<RecipeStepResponseDto> recipeStepsResponseDto = List.of(this.recipeStep);
        //Mock
        when(recipeStepClient.getRecipeStepsByRecipeId(recipeId)).thenReturn(recipeStepsResponseDto);
        //Act
        List<RecipeStepResponseDto> result = recipeStepService.getRecipeStepsByRecipeId(recipeId);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream()
                .anyMatch(rsDto -> rsDto.getId().equals(this.recipeStep.getId())));
        assertTrue(result.stream()
                .anyMatch(rsDto -> rsDto.getImgUrl().equals(this.recipeStep.getImgUrl())));
        assertTrue(result.stream()
                .anyMatch(rsDto -> rsDto.getStepNumber().equals(this.recipeStep.getStepNumber())));
        assertTrue(result.stream()
                .anyMatch(rsDto -> rsDto.getDescription().equals(this.recipeStep.getDescription())));
        //Verify
        verify(recipeStepClient).getRecipeStepsByRecipeId(recipeId);
    }
}