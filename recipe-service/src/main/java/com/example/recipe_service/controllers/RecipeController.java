package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.recipe.RecipeCreateRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeSimpleResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeUpdateRequestDto;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.services.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

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
    public ResponseEntity<Page<RecipeSimpleResponseDto>> getAllRecipes(Pageable pageable){
        Page<RecipeSimpleResponseDto> categories = recipeService.getAllSimpleRecipes(pageable);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<Page<RecipeSimpleResponseDto>> getRecipesByCategoryId(@PathVariable("categoryId") Integer categoryId,
                                                                                Pageable pageable){
        Page<RecipeSimpleResponseDto> recipesSimpleResponseDto = recipeService.getRecipesByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(recipesSimpleResponseDto);
    }

    @PutMapping("/{recipeId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRecipeById(@PathVariable("recipeId") Integer recipeId,
                                 @RequestBody RecipeUpdateRequestDto recipeUpdateRequestDto){
        Recipe recipe = recipeService.getRecipeById(recipeId);
        recipeService.updateRecipe(recipe, recipeUpdateRequestDto);
    }

    @DeleteMapping("/{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipeById(@PathVariable("recipeId") Integer recipeId){
        recipeService.deleteRecipeById(recipeId);
    }
}
