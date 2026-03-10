package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.ingredient.IngredientRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeSimpleResponseDto;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.services.RecipeService;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecipe(@RequestBody RecipeRequestDto recipeRequestDto){
        recipeService.createRecipe(recipeRequestDto);
    }

    @PostMapping("/add-ingredient/{recipeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addIngredientToRecipe(@PathVariable("recipeId") Integer recipeId,
                                      @RequestBody IngredientRequestDto ingredientRequestDto){
        recipeService.addIngredientToRecipe(recipeId, ingredientRequestDto);
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes(){
        List<Recipe> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable ("recipeId") Integer recipeId){
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/with-categories")
    public ResponseEntity<List<RecipeSimpleResponseDto>> getAllRecipesWithCategories(){
        List<RecipeSimpleResponseDto> categories = recipeService.getAllRecipesWithCategories();
        return ResponseEntity.ok(categories);
    }
}
