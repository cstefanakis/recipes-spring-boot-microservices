package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.category.CategoryRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeCreateRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeGlobalResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeUpdateRequestDto;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.services.CategoryService;
import com.example.recipe_service.services.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final CategoryService categoryService;


    //Recipes
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecipe(@Valid @RequestBody RecipeCreateRequestDto recipeRequestDto){
        recipeService.createRecipe(recipeRequestDto);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponseDto> getRecipeById(@PathVariable ("recipeId") Integer recipeId){
        Recipe recipe = recipeService.getRecipeById(recipeId);
        RecipeResponseDto recipeDto = recipeService.toRecipeResponseDto(recipe);
        return ResponseEntity.ok(recipeDto);
    }

    @GetMapping
    public ResponseEntity<List<RecipeGlobalResponseDto>> getAllRecipes(){
        List<RecipeGlobalResponseDto> categories = recipeService.getAllRecipesWithCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{recipeId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRecipeById(@PathVariable("recipeId") Integer recipeId,
                                 @RequestBody RecipeUpdateRequestDto recipeUpdateRequestDto){
        Recipe recipe = recipeService.getRecipeById(recipeId);
        recipeService.updateRecipe(recipe, recipeUpdateRequestDto);
    }


    //Categories
    @PostMapping("/category")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto){
        categoryService.createCategory(categoryRequestDto);
    }
}
