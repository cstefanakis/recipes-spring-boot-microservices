package com.recipe.ingredients.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("recipe-service")
public interface RecipeClient {

    @GetMapping("/api/recipe-ingredients/ingredient-id-exists/{ingredientId}")
    boolean ingredientIdExists(@PathVariable("ingredientId") Integer ingredientId);
}
