package com.example.recipe_service.clients;

import com.example.recipe_service.dtos.recipeStep.RecipeStepCreateRequestDto;
import com.example.recipe_service.dtos.recipeStep.RecipeStepResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "recipe-step-service")
public interface RecipeStepClient {

    @GetMapping("/api/recipe-steps/recipe/{recipeId}")
    List<RecipeStepResponseDto> getRecipeStepsByRecipeId(@PathVariable ("recipeId") Integer recipeId);

    @PutMapping("/api/recipe-step/{id}")
    void updateRecipeStepById(@PathVariable ("id") Integer id);

    @PostMapping("/api/recipe-steps")
    RecipeStepResponseDto createRecipeStep(@RequestBody RecipeStepCreateRequestDto recipeStepCreateRequestDto);

    @DeleteMapping("/api/recipe-steps/{id}")
    void deleteRecipeStepById(@PathVariable("id") Integer id);
}
