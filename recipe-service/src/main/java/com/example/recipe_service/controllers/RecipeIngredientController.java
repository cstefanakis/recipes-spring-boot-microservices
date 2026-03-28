package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientCreateRequestDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientUpdateRequestDto;
import com.example.recipe_service.models.RecipeIngredient;
import com.example.recipe_service.services.RecipeIngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<RecipeIngredientResponseDto>> getRecipeIngredients(){
        List<RecipeIngredient> recipeIngredients = recipeIngredientService.getAllRecipeIngredients();
        List<RecipeIngredientResponseDto> recipeIngredientsDto = recipeIngredients.stream()
                .map(recipeIngredientService::toRecipeIngredientResponseDto)
                .toList();
        return ResponseEntity.ok(recipeIngredientsDto);
    }

    @DeleteMapping("/{recipeIngredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipeIngredientById(@PathVariable("recipeIngredientId") Integer recipeIngredientId){
        recipeIngredientService.deleteRecipeIngredientById(recipeIngredientId);
    }

    @PutMapping("/{recipeIngredientId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRecipeIngredientById(@PathVariable("recipeIngredientId") Integer recipeIngredientId,
                                           @Valid @RequestBody RecipeIngredientUpdateRequestDto recipeIngredientUpdateRequestDto){
        RecipeIngredient recipeIngredient = recipeIngredientService.getRecipeIngredientById(recipeIngredientId);
        recipeIngredientService.updateRecipeIngredient(recipeIngredient, recipeIngredientUpdateRequestDto);
    }
}
