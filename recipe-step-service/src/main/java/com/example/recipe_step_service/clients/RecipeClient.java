package com.example.recipe_step_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "recipe-service")
public interface RecipeClient {

    @GetMapping("/api/recipes/exists/{recipeId}")
    boolean recipeExists(@PathVariable("recipeId") Integer recipeId);
}
