package com.recipe.recipe.services;

import com.recipe.recipe.clients.RecipeStepClient;
import com.recipe.recipe.dtos.recipeStep.RecipeStepResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeStepService {

    private final RecipeStepClient recipeStepClient;

    public List<RecipeStepResponseDto> getRecipeStepsByRecipeId(Integer recipeId) {
        return recipeStepClient.getRecipeStepsByRecipeId(recipeId);
    }
}
