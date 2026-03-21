package com.example.recipe_service.services;

import com.example.recipe_service.clients.RecipeStepClient;
import com.example.recipe_service.dtos.recipeStep.RecipeStepResponseDto;
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
