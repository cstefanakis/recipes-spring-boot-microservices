package com.example.recipe_service.services;

import com.example.recipe_service.clients.IngredientClient;
import com.example.recipe_service.dtos.ingredient.IngredientGlobalResponseDto;
import com.example.recipe_service.dtos.ingredient.IngredientResponseDto;
import com.example.recipe_service.models.RecipeIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeIngredientService {

    private final IngredientClient ingredientClient;

    public IngredientGlobalResponseDto toIngredientGlobalResponseDto(RecipeIngredient recipeIngredient) {
        return ingredientClient.getGlobalIngredientById(recipeIngredient.getIngredientId());
    }

    public IngredientResponseDto toIngredientResponseDto(RecipeIngredient recipeIngredient) {
        IngredientGlobalResponseDto ingredientGlobalResponseDto = toIngredientGlobalResponseDto(recipeIngredient);
        return IngredientResponseDto.builder()
                .name(ingredientGlobalResponseDto.getName())
                .imgUrl(ingredientGlobalResponseDto.getImgUrl())
                .quantity(recipeIngredient.getQuantity())
                .unit(recipeIngredient.getUnit().toString())
                .build();
    }
}
