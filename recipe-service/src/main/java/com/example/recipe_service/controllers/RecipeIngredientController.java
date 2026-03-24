package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientCreateRequestDto;
import com.example.recipe_service.services.RecipeIngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe-ingredients")
@RequiredArgsConstructor
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecipeIngredient(@Valid @RequestBody RecipeIngredientCreateRequestDto recipeIngredientCreateRequestDto){
        recipeIngredientService.createRecipeIngredient(recipeIngredientCreateRequestDto);
    }
}
