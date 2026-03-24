package com.example.recipe_service.services;

import com.example.recipe_service.clients.IngredientClient;
import com.example.recipe_service.dtos.ingredient.IngredientSimpleResponseDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientCreateRequestDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.models.RecipeIngredient;
import com.example.recipe_service.repositories.RecipeIngredientRepository;
import com.example.recipe_service.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.example.recipe_service.enums.Unit.GRAM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RecipeIngredientServiceTest {

    @InjectMocks
    private RecipeIngredientService recipeIngredientService;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientClient ingredientClient;

    private Recipe recipe;
    private RecipeIngredient recipeIngredient;
    private Category category;
    private IngredientSimpleResponseDto ingredient;

    @BeforeEach
    void setup(){
        this.category = Category.builder()
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
                .build();

        this.recipeIngredient = RecipeIngredient.builder()
                .id(1)
                .ingredientId(1)
                .unit(GRAM)
                .quantity(500.0)
                .recipe(this.recipe)
                .build();

        this.ingredient = IngredientSimpleResponseDto.builder()
                .id(1)
                .name("ingredient")
                .imgUrl("url")
                .build();
    }

    @Test
    void createRecipeIngredient() {
        Integer recipeId = this.recipe.getId();

        RecipeIngredientCreateRequestDto recipeIngredientCreateRequestDto = RecipeIngredientCreateRequestDto.builder()
                .ingredientId(1)
                .recipeId(this.recipe.getId())
                .quantity(this.recipeIngredient.getQuantity())
                .unit(this.recipeIngredient.getUnit())
                .build();
        //Mock
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(this.recipe));
        when(recipeIngredientRepository.save(any(RecipeIngredient.class))).thenReturn(this.recipeIngredient);
        //Act
        recipeIngredientService.createRecipeIngredient(recipeIngredientCreateRequestDto);
        //Verify
        verify(recipeRepository).findById(recipeId);
        verify(recipeIngredientRepository).save(any(RecipeIngredient.class));
    }

    @Test
    void getRecipeIngredientsByRecipeId() {
        //Arrest
        List<RecipeIngredient> recipeIngredients = List.of(this.recipeIngredient);
        Integer recipeId = this.recipe.getId();
        //Mock
        when(recipeIngredientRepository.findRecipeIngredientsByRecipeId(recipeId))
                .thenReturn(recipeIngredients);
        when(ingredientClient.getIngredientById(this.recipeIngredient.getIngredientId()))
                .thenReturn(this.ingredient);
        //Act
        List<RecipeIngredientResponseDto> result = recipeIngredientService.getRecipeIngredientsByRecipeId(recipeId);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getName().equals(this.ingredient.getName())));
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getImgUrl().equals(this.ingredient.getImgUrl())));
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getQuantity().equals(this.recipeIngredient.getQuantity())));
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getUnit().equals(this.recipeIngredient.getUnit())));
        //Verify
        verify(recipeIngredientRepository).findRecipeIngredientsByRecipeId(recipeId);
    }
}