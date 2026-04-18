package com.recipe.recipe.clients;

import com.recipe.recipe.dtos.ingredient.IngredientSimpleResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ingredients-service")
public interface IngredientClient {

    @GetMapping("/api/ingredients/{ingredientId}")
    IngredientSimpleResponseDto getIngredientById(@PathVariable("ingredientId") Integer ingredientId);

    @GetMapping("/api/ingredients/exists/{ingredientId}")
    Integer ingredientExistById(@PathVariable("ingredientId") Integer ingredientId);
}
