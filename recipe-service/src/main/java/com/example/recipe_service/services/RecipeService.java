package com.example.recipe_service.services;

import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeCreateRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeSimpleResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeUpdateRequestDto;
import com.example.recipe_service.dtos.recipeStep.RecipeStepResponseDto;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.repositories.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                .imgUrl(recipeRequestDto.getImgUrl())
                .build();
    }

    public void updateRecipe(Recipe recipe, RecipeUpdateRequestDto recipeUpdateRequestDto){

        String titleDto = recipeUpdateRequestDto.getTitle();
        String descriptionDto = recipeUpdateRequestDto.getDescription();
        String imgUrlDto = recipeUpdateRequestDto.getImgUrl();
        List<Category> categories = categoryService.getCategoriesByIds(recipeUpdateRequestDto.getCategoriesId());


        recipe.setTitle(titleDto == null
                ? recipe.getTitle()
                : titleDto);
        recipe.setDescription(descriptionDto == null
                ? recipe.getDescription()
                : descriptionDto);
        recipe.setImgUrl(imgUrlDto == null
                ? recipe.getImgUrl()
                : imgUrlDto);
        recipe.setCategories(categories);

        recipeRepository.save(recipe);
    }

    public Recipe getRecipeById(Integer recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Recipe with id: %s doesn't found", recipeId)));
    }

    private List<CategoryResponseDto> toCategoriesResponseDto(List<Category> categories){
        return categories.stream()
                .map(categoryService::toCategoryResponseDto)
                .toList();
    }

    public void deleteRecipeById(Integer recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    public Page<RecipeSimpleResponseDto> getAllSimpleRecipes(Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findAll(pageable);
        return recipes.map(this::toRecipeSimpleResponseDto);
    }

    private RecipeSimpleResponseDto toRecipeSimpleResponseDto(Recipe recipe) {
        return RecipeSimpleResponseDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .imgUrl(recipe.getImgUrl())
                .description(recipe.getDescription())
                .build();
    }

    public RecipeResponseDto toRecipeResponseDto(Recipe recipe) {

        List<RecipeIngredientResponseDto> ingredients = recipeIngredientService.getRecipeIngredientsByRecipeId(recipe.getId());

        List<CategoryResponseDto> categories = recipe.getCategories().stream()
                .map(categoryService::toCategoryResponseDto)
                .toList();

        List<RecipeStepResponseDto> recipeSteps = recipeStepService.getRecipeStepsByRecipeId(recipe.getId());

        return RecipeResponseDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .imgUrl(recipe.getImgUrl())
                .description(recipe.getDescription())
                .ingredients(ingredients)
                .categories(categories)
                .recipeSteps(recipeSteps)
                .build();
    }

    public Page<RecipeSimpleResponseDto> getRecipesByCategoryId(Integer categoryId, Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findRecipesByCategoryId(categoryId, pageable);
        return recipes.map(this::toRecipeSimpleResponseDto);
    }

    public boolean recipeExists(Integer recipeId) {
        return recipeRepository.existsById(recipeId);
    }
}
