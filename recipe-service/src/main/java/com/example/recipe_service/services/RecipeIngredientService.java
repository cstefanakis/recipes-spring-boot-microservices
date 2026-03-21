package com.example.recipe_service.services;

import com.example.recipe_service.clients.IngredientClient;
import com.example.recipe_service.dtos.ingredient.IngredientSimpleResponseDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientCreateRequestDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.models.RecipeIngredient;
import com.example.recipe_service.repositories.RecipeIngredientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeIngredientService {

    private final IngredientClient ingredientClient;
    private final RecipeIngredientRepository recipeIngredientRepository;


    public void createRecipeIngredient(RecipeIngredientCreateRequestDto ingredientCreateRequestDto) {
        RecipeIngredient recipeIngredient = toEntity(ingredientCreateRequestDto);
        recipeIngredientRepository.save(recipeIngredient);
    }

    private RecipeIngredient toEntity(RecipeIngredientCreateRequestDto ingredientCreateRequestDto) {

        return RecipeIngredient.builder()
                .ingredientId(ingredientCreateRequestDto.getIngredientId())
                .recipe(ingredientCreateRequestDto.getRecipe())
                .unit(ingredientCreateRequestDto.getUnit())
                .quantity(ingredientCreateRequestDto.getQuantity())
                .build();
    }

    public List<RecipeIngredientResponseDto> getRecipeIngredientsByRecipeId(Integer recipeId) {
        List<RecipeIngredient> ingredients = recipeIngredientRepository.findRecipeIngredientsByRecipeId(recipeId);
        return ingredients.stream()
                .map(this::toIngredientResponseDto)
                .toList();
    }

    private RecipeIngredientResponseDto toIngredientResponseDto(RecipeIngredient recipeIngredient) {

        IngredientSimpleResponseDto ingredientSimpleResponseDto = ingredientClient.getIngredientById(recipeIngredient.getId());

        return RecipeIngredientResponseDto.builder()
                .name(ingredientSimpleResponseDto.getName())
                .imgUrl(ingredientSimpleResponseDto.getImgUrl())
                .unit(recipeIngredient.getUnit())
                .quantity(recipeIngredient.getQuantity())
                .build();
    }
}
