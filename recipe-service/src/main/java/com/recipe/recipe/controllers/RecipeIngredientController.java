package com.recipe.recipe.controllers;

import com.recipe.recipe.dtos.recipeIngredient.RecipeIngredientCreateRequestDto;
import com.recipe.recipe.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.recipe.recipe.dtos.recipeIngredient.RecipeIngredientUpdateRequestDto;
import com.recipe.recipe.models.RecipeIngredient;
import com.recipe.recipe.services.RecipeIngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-ingredients")
@RequiredArgsConstructor
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecipeIngredient(@Valid @RequestBody RecipeIngredientCreateRequestDto recipeIngredientCreateRequestDto){

        recipeIngredientService.createRecipeIngredient(recipeIngredientCreateRequestDto);

    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<RecipeIngredientResponseDto>> getRecipeIngredients(){

        List<RecipeIngredientResponseDto> recipeIngredientsDto = recipeIngredientService.getRecipeIngredients();

        return ResponseEntity.ok(recipeIngredientsDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{recipeIngredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipeIngredientById(@PathVariable("recipeIngredientId") Integer recipeIngredientId){

        recipeIngredientService.deleteRecipeIngredientById(recipeIngredientId);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{recipeIngredientId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRecipeIngredientById(@PathVariable("recipeIngredientId") Integer recipeIngredientId,
                                           @Valid @RequestBody RecipeIngredientUpdateRequestDto recipeIngredientUpdateRequestDto){

        RecipeIngredient recipeIngredient =
                recipeIngredientService.getRecipeIngredientById(recipeIngredientId);

        recipeIngredientService.updateRecipeIngredient(recipeIngredient, recipeIngredientUpdateRequestDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/ingredient-id-exists/{ingredientId}")
    public ResponseEntity<Boolean> ingredientIdExists(@PathVariable("ingredientId") Integer ingredientId){

        boolean ingredientIdExists =
                recipeIngredientService.recipeIngredientWithIngredientIdExists(ingredientId);

        return ResponseEntity.ok(ingredientIdExists);
    }
}
