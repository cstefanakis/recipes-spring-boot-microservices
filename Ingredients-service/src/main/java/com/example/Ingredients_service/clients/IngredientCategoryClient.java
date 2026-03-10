package com.example.Ingredients_service.clients;

import com.example.Ingredients_service.dtos.IngredientCategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ingredient-categories-service")
public interface IngredientCategoryClient {

    @GetMapping("/api/ingredient-categories/{categoryId}")
    IngredientCategoryDto getIngredientCategoryById(@PathVariable ("categoryId") Integer categoryId);

    @GetMapping("/api/ingredient-categories/exists/{categoryId}")
    boolean ingredientCategoryExistsByCategoryId(@PathVariable ("categoryId") Integer categoryId);
}
