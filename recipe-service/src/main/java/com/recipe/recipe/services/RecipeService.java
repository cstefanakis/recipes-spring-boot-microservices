package com.recipe.recipe.services;

import com.recipe.recipe.clients.RecipeStepClient;
import com.recipe.recipe.dtos.category.CategoryResponseDto;
import com.recipe.recipe.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.recipe.recipe.dtos.recipe.RecipeCreateRequestDto;
import com.recipe.recipe.dtos.recipe.RecipeResponseDto;
import com.recipe.recipe.dtos.recipe.RecipeSimpleResponseDto;
import com.recipe.recipe.dtos.recipe.RecipeUpdateRequestDto;
import com.recipe.recipe.dtos.recipeStep.RecipeStepResponseDto;
import com.recipe.recipe.dtos.user.UserResponseIdAndRole;
import com.recipe.recipe.dtos.user.UserResponseIdAndUsernameDto;
import com.recipe.recipe.models.Category;
import com.recipe.recipe.models.Recipe;
import com.recipe.recipe.repositories.RecipeRepository;
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
    private final UserService userService;

    public void createRecipe(RecipeCreateRequestDto recipeRequestDto) {
        Recipe recipe = toEntity(recipeRequestDto);
        recipeRepository.save(recipe);
    }

    private Recipe toEntity(RecipeCreateRequestDto recipeRequestDto) {

        List<Category> categories = recipeRequestDto.getCategoriesId().stream()
                .map(categoryService::getCategoryById)
                .toList();

        UserResponseIdAndRole authenticatedUser = userService.getAuthenticatedUser();

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

        boolean isOwnerOrAdmin = userService.isOwnerOrAdmin(recipeOwnerId);

        if(isOwnerOrAdmin){

            String titleDto = recipeUpdateRequestDto.getTitle();
            String descriptionDto = recipeUpdateRequestDto.getDescription();
            String imgUrlDto = recipeUpdateRequestDto.getImgUrl();
            Integer authorId = recipeUpdateRequestDto.getAuthorId();
            List<Category> categories = categoryService.getCategoriesByIds(recipeUpdateRequestDto.getCategoriesId());

            recipe.setTitle(updatedTitle(recipe.getTitle(), titleDto));

            recipe.setDescription(descriptionDto == null
                    ? recipe.getDescription()
                    : descriptionDto);

            recipe.setImgUrl(imgUrlDto == null
                    ? recipe.getImgUrl()
                    : imgUrlDto);

            recipe.setUserId(authorId == null
                    ? recipe.getUserId()
                    : authorId);

            recipe.setCategories(categories);

            recipeRepository.save(recipe);

        } else {
            throw new RuntimeException("You don't have permission");
        }
    }

    private String updatedTitle(String title, String titleDto) {

        if(titleDto == null){
            return title;
        }

        if(title.equals(titleDto)){
            return title;
        }

        validatedTitle(titleDto);

        return titleDto;
    }

    public Recipe getRecipeById(Integer recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Recipe with id: %s doesn't found", recipeId)));
    }

    public void deleteRecipeById(Integer recipeId) {

        Integer recipeOwnerId = recipeRepository.findRecipeOwnerIdByRecipeId(recipeId);

        boolean isOwnerOrAdmin = userService.isOwnerOrAdmin(recipeOwnerId);

        if(isOwnerOrAdmin) {
            recipeRepository.deleteById(recipeId);
            recipeStepClient.deleteAllByRecipeId(recipeId);
        } else {
            throw new RuntimeException("You don't have permission");
        }
    }

    public Page<RecipeSimpleResponseDto> getAllSimpleRecipes(Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findAll(pageable);
        return recipes.map(this::toRecipeSimpleResponseDto);
    }

    private RecipeSimpleResponseDto toRecipeSimpleResponseDto(Recipe recipe) {

        UserResponseIdAndUsernameDto userDto = getUserResponseIdAndUsernameDtoByUserId(recipe.getUserId());

        return RecipeSimpleResponseDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .imgUrl(recipe.getImgUrl())
                .description(recipe.getDescription())
                .author(userDto)
                .build();
    }

    private UserResponseIdAndUsernameDto getUserResponseIdAndUsernameDtoByUserId(Integer userId) {
        try {
            return userService.getUserIdAndUsernameByUserId(userId);
        }catch (Exception e){
            return UserResponseIdAndUsernameDto.builder()
                    .username("Unknown author")
                    .build();
        }
    }

    public RecipeResponseDto toRecipeResponseDto(Recipe recipe) {

        List<RecipeIngredientResponseDto> ingredients =
                recipeIngredientService.getRecipeIngredientsByRecipeId(recipe.getId());

        List<CategoryResponseDto> categories = recipe.getCategories().stream()
                .map(categoryService::toCategoryResponseDto)
                .toList();

        List<RecipeStepResponseDto> recipeSteps =
                recipeStepService.getRecipeStepsByRecipeId(recipe.getId());

        UserResponseIdAndUsernameDto author = getUserResponseIdAndUsernameDtoByUserId(recipe.getUserId());

        return RecipeResponseDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .imgUrl(recipe.getImgUrl())
                .author(author)
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

    public Integer getRecipeOwnerIdByRecipeId(Integer recipeId) {
        return recipeRepository.findRecipeOwnerIdByRecipeId(recipeId);
    }

    public Page<RecipeSimpleResponseDto> getAllAuthorSimpleRecipes(Integer userId, Pageable pageable) {

        Page<Recipe> recipes = recipeRepository.findAllAuthorSimpleRecipes(userId, pageable);

        return recipes.map(this::toRecipeSimpleResponseDto);
    }
}
