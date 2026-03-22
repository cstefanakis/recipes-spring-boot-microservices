package com.example.recipe_service.services;

import com.example.recipe_service.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Test
    void createRecipe() {
    }

    @Test
    void updateRecipe() {
    }

    @Test
    void getRecipeById() {
    }

    @Test
    void deleteRecipeById() {
    }

    @Test
    void getAllSimpleRecipes() {
    }

    @Test
    void toRecipeResponseDto() {
    }

    @Test
    void getRecipesByCategoryId() {
    }
}