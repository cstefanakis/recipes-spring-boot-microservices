package com.example.recipe_service.services;

import com.example.recipe_service.clients.RecipeStepClient;
import com.example.recipe_service.clients.UserClient;
import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeCreateRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeSimpleResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeUpdateRequestDto;
import com.example.recipe_service.dtos.recipeStep.RecipeStepResponseDto;
import com.example.recipe_service.dtos.user.UserResponseIdAndRole;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.repositories.RecipeRepository;
import jakarta.persistence.EntityExistsException;
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
    private final RecipeStepClient recipeStepClient;
    private final UserClient userClient;

    public void createRecipe(RecipeCreateRequestDto recipeRequestDto) {
        Recipe recipe = toEntity(recipeRequestDto);
        recipeRepository.save(recipe);
    }

    private Recipe toEntity(RecipeCreateRequestDto recipeRequestDto) {

        List<Category> categories = recipeRequestDto.getCategoriesId().stream()
                .map(categoryService::getCategoryById)
                .toList();

        UserResponseIdAndRole authenticatedUser = userClient.authenticatedUserIdAndRole().getBody();

        return Recipe.builder()
                .title(validatedTitle(recipeRequestDto.getTitle()))
                .description(recipeRequestDto.getDescription())
                .categories(categories)
                .imgUrl(recipeRequestDto.getImgUrl())
                .userId(authenticatedUser.getId())
                .build();
    }

    private String validatedTitle(String title) {
        boolean titleExists = recipeRepository.titleExists(title);
        if(titleExists){
            throw new EntityExistsException(String.format("Recipe With title %s already exists", title));
        }
        return title;
    }

    public void updateRecipe(Recipe recipe, RecipeUpdateRequestDto recipeUpdateRequestDto){

        Integer recipeOwnerId = recipe.getUserId();

        if(isOwnerOrAdmin(recipeOwnerId)){

            String titleDto = recipeUpdateRequestDto.getTitle();
            String descriptionDto = recipeUpdateRequestDto.getDescription();
            String imgUrlDto = recipeUpdateRequestDto.getImgUrl();
            List<Category> categories = categoryService.getCategoriesByIds(recipeUpdateRequestDto.getCategoriesId());

            recipe.setTitle(titleDto == null
                    ? recipe.getTitle()
                    : validatedTitle(titleDto));
            recipe.setDescription(descriptionDto == null
                    ? recipe.getDescription()
                    : descriptionDto);
            recipe.setImgUrl(imgUrlDto == null
                    ? recipe.getImgUrl()
                    : imgUrlDto);
            recipe.setCategories(categories);

            recipeRepository.save(recipe);
        }
    }

    private boolean isOwnerOrAdmin(Integer recipeOwnerId) {

        UserResponseIdAndRole authenticatedUser = userClient.authenticatedUserIdAndRole().getBody();

        return authenticatedUser.getId().equals(recipeOwnerId)
                || authenticatedUser.getRole().equals("ADMIN");
    }

    public Recipe getRecipeById(Integer recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Recipe with id: %s doesn't found", recipeId)));
    }

    public void deleteRecipeById(Integer recipeId) {

        Integer recipeOwnerId = recipeRepository.findRecipeOwnerIdByRecipeId(recipeId);

        if(isOwnerOrAdmin(recipeOwnerId)) {
            recipeRepository.deleteById(recipeId);
            recipeStepClient.deleteAllByRecipeId(recipeId);
        }
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
