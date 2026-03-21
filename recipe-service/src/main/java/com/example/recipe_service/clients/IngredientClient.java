package com.example.recipe_service.clients;

import com.example.recipe_service.dtos.ingredient.IngredientSimpleResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ingredients-service")
public interface IngredientClient {

    @GetMapping("/api/ingredients/simple/{ingredientId}")
    IngredientSimpleResponseDto getIngredientById(@PathVariable("ingredientId") Integer ingredientId);
}
