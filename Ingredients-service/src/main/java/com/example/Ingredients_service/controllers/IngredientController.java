package com.example.Ingredients_service.controllers;

import com.example.Ingredients_service.dtos.IngredientDto;
import com.example.Ingredients_service.dtos.IngredientResponseDto;
import com.example.Ingredients_service.dtos.IngredientUpdateDto;
import com.example.Ingredients_service.models.Ingredient;
import com.example.Ingredients_service.services.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createIngredient(@RequestBody IngredientDto ingredientDto){
        ingredientService.createIngredient(ingredientDto);
    }

    @GetMapping("/with-categories/{ingredientId}")
    public ResponseEntity<IngredientResponseDto> getIngredientWithCategoryById(
            @PathVariable ("ingredientId") Integer ingredientId){

        IngredientResponseDto ingredient = ingredientService.getIngredientWithCategoryById(ingredientId);

        return ResponseEntity.ok(ingredient);
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients(){
        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponseDto>> getAllIngredientsWithCategories(){
        List<IngredientResponseDto> ingredients = ingredientService.getAllIngredientsWithCategories();
        return ResponseEntity.ok(ingredients);
    }

    @DeleteMapping("/{ingredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable ("ingredientId") Integer ingredientId){
        ingredientService.deleteIngredient(ingredientId);
    }

    @PutMapping("/{ingredientId}")
    public ResponseEntity<Ingredient> updateIngredient(
            @PathVariable ("ingredientId") Integer ingredientId,
            @RequestBody IngredientUpdateDto ingredientUpdateDto){
        Ingredient updatedIngredient = ingredientService.updateIngredient(ingredientId, ingredientUpdateDto);
        return ResponseEntity.ok(updatedIngredient);
    }
}
