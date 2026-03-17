package com.example.recipe_service.services;

import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.dtos.ingredient.IngredientResponseDto;
import com.example.recipe_service.dtos.ingredient.IngredientGlobalResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeCreateRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeGlobalResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeUpdateRequestDto;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.models.RecipeIngredient;
import com.example.recipe_service.models.RecipeStep;
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
    private final RecipeStepService recipeStepService;

    public void createRecipe(RecipeCreateRequestDto recipeRequestDto) {
        Recipe recipe = toEntity(recipeRequestDto);
        recipeRepository.save(recipe);
    }

    private Recipe toEntity(RecipeCreateRequestDto recipeRequestDto) {

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

    public void updateRecipe(Recipe recipe, RecipeUpdateRequestDto recipeUpdateRequestDto){

        String titleDto = recipeUpdateRequestDto.getTitle();
        String descriptionDto = recipeUpdateRequestDto.getDescription();
        String imgUrlDto = recipeUpdateRequestDto.getImgUrl();
        List<RecipeStep> recipeSteps = recipeStepService.updateRecipeSteps(recipe.getRecipeSteps(), recipeUpdateRequestDto.getRecipeSteps());
        List<RecipeIngredient> recipeIngredients = recipeIngredientService.updateRecipeIngredients(recipe.getIngredients(), recipeUpdateRequestDto.getIngredients());
        List<Category> categories = categoryService.updateCategories(recipe.getCategories(), recipeUpdateRequestDto.getCategoriesId());

        recipe.setTitle(titleDto == null
                ? recipe.getTitle()
                : titleDto);
        recipe.setDescription(descriptionDto == null
                ? recipe.getDescription()
                : descriptionDto);
        recipe.setImgUrl(imgUrlDto == null
                ? recipe.getImgUrl()
                : imgUrlDto);
        recipe.setRecipeSteps(recipeSteps);
        recipe.setIngredients(recipeUpdateRequestDto.getIngredients());
        recipe.setCategories(recipeUpdateRequestDto.getCategoriesId())
                .build();
    }

    public Recipe getRecipeById(Integer recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Recipe with id: %s doesn't found", recipeId)));
    }

    public List<RecipeGlobalResponseDto> getAllRecipesWithCategories() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream()
                .map(this::toRecipeGlobalResponseDto)
                .toList();
    }

    private RecipeGlobalResponseDto toRecipeGlobalResponseDto(Recipe recipe) {

        List<IngredientGlobalResponseDto> ingredientsDto = recipe.getIngredients().stream()
                .map(recipeIngredientService::toIngredientGlobalResponseDto)
                .toList();

        List<Category> categories = recipe.getCategories();

        List<CategoryResponseDto> categoriesDto = toCategoriesResponseDto(categories);

        return RecipeGlobalResponseDto.builder()
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .imgUrl(recipe.getImgUrl())
                .ingredients(ingredientsDto)
                .categories(categoriesDto)
                .build();
    }

    private List<CategoryResponseDto> toCategoriesResponseDto(List<Category> categories){
        return categories.stream()
                .map(categoryService::toCategoryResponseDto)
                .toList();
    }

    public RecipeResponseDto toRecipeResponseDto(Recipe recipe) {

        List <IngredientResponseDto> ingredients = recipe.getIngredients().stream()
                .map(recipeIngredientService::toIngredientResponseDto)
                .toList();

        List<Category> categories = recipe.getCategories();

        List<CategoryResponseDto> categoriesDto = toCategoriesResponseDto(categories);

        return RecipeResponseDto.builder()
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .imgUrl(recipe.getImgUrl())
                .ingredients(ingredients)
                .categories(categoriesDto)
                .build();
    }
}
