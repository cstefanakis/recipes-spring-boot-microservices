package com.recipe.recipe.clients;

import com.recipe.recipe.dtos.recipeStep.RecipeStepResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "recipe-step-service")
public interface RecipeStepClient {

    @GetMapping("/api/recipe-steps/recipe/{recipeId}")
    List<RecipeStepResponseDto> getRecipeStepsByRecipeId(@PathVariable("recipeId") Integer recipeId);

    @DeleteMapping("/api/recipe-steps/delete-all/{recipeId}")
    void deleteAllByRecipeId(@PathVariable("recipeId") Integer recipeId);
}
