package com.example.recipe_service.services;

import com.example.recipe_service.dtos.ingredient.IngredientSimpleResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeCreateRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeUpdateRequestDto;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.models.RecipeIngredient;
import com.example.recipe_service.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.recipe_service.enums.Unit.GRAM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CategoryService categoryService;

    private Recipe recipe;
    private RecipeIngredient recipeIngredient;
    private Category category;

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
    }

    @Test
    void createRecipe() {
        // Arrange
        RecipeCreateRequestDto recipeCreateRequestDto = RecipeCreateRequestDto.builder()
                .title(this.recipe.getTitle())
                .description(this.recipe.getDescription())
                .imgUrl(this.recipe.getImgUrl())
                .categoriesId(List.of(this.category.getId()))
                .build();

        //Mock
        when(categoryService.existsCategoryById(this.category.getId())).thenReturn(true);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(this.recipe);

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        //Act
        recipeService.createRecipe(recipeCreateRequestDto);

        //Verify
        verify(categoryService, times(1)).existsCategoryById(this.category.getId());
        verify(recipeRepository).save(captor.capture());

        Recipe result = captor.getValue();
        //Assert
        assertEquals(this.recipe.getTitle(), result.getTitle());
        assertEquals(this.recipe.getDescription(), result.getDescription());
        assertEquals(this.recipe.getImgUrl(), result.getImgUrl());
    }

    @Test
    void updateRecipe() {
        //Arrest
        RecipeUpdateRequestDto recipeUpdateRequestDto = RecipeUpdateRequestDto.builder()
                .title("pizza")
                .description("pizza from Chris")
                .imgUrl("icon")
                .categoriesId(List.of(1))
                .build();

        List<Category> categories = List.of(this.category);

        //Mock
        when(categoryService.getCategoriesByIds(recipeUpdateRequestDto.getCategoriesId()))
                .thenReturn(categories);
        when(recipeRepository.save(this.recipe))
                .thenReturn(this.recipe);
        //Act
        recipeService.updateRecipe(this.recipe, recipeUpdateRequestDto);
        //Assert
        assertEquals("pizza", this.recipe.getTitle());
        assertEquals("pizza from Chris", this.recipe.getDescription());
        assertEquals("icon", this.recipe.getImgUrl());
        assertEquals(1, this.recipe.getCategories().size());
        //Verify
        verify(categoryService).getCategoriesByIds(recipeUpdateRequestDto.getCategoriesId());
        verify(recipeRepository).save(this.recipe);

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