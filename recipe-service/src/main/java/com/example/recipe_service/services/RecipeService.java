package com.example.recipe_service.services;

import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.dtos.ingredient.IngredientRequestDto;
import com.example.recipe_service.dtos.ingredient.IngredientSimpleResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeSimpleResponseDto;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.repositories.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryService categoryService;
    private final RecipeIngredientService recipeIngredientService;

    public void createRecipe(RecipeRequestDto recipeRequestDto) {
        Recipe recipe = toEntity(recipeRequestDto);
        recipeRepository.save(recipe);
    }

    private Recipe toEntity(RecipeRequestDto recipeRequestDto) {

        List<Category> categories = recipeRequestDto.getCategoriesId().stream()
                .filter(categoryService::existsCategoryById)
                .map(categoryService::getCategoryById)
                .toList();

        return Recipe.builder()
                .title(recipeRequestDto.getTitle())
                .description(recipeRequestDto.getDescription())
                .categories(categories)
                .build();
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(Integer recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Recipe with id: %s doesn't found", recipeId)));
    }

    public List<RecipeSimpleResponseDto> getAllRecipesWithCategories() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream()
                .map(this::toRecipeSimpleResponseDto)
                .toList();
    }

    private RecipeSimpleResponseDto toRecipeSimpleResponseDto(Recipe recipe) {

        List<IngredientSimpleResponseDto> ingredients = recipe.getIngredientsId().stream()
                .map(ingredientId -> recipeIngredientService.toIngredientSimpleResponseDto(ingredientId))
                .toList();

        List<CategoryResponseDto> categories = recipe.getCategories().stream()
                .map(categoryId -> categoryService.toCategoryResponseDto)
                .toList();

        return RecipeSimpleResponseDto.builder()
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .imgUrl(recipe.getImgUrl())
                .ingredients(ingredients)
                .categories(categories)
                .build();
    }

    public void addIngredientToRecipe(Integer recipeId, IngredientRequestDto ingredientRequestDto) {
    }
}
