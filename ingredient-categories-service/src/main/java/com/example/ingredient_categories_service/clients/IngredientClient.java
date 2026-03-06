package com.example.ingredient_categories_service.clients;

import com.example.ingredient_categories_service.dtos.IngredientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ingredients-service")
public interface IngredientClient {

    @GetMapping("/api/ingredients/simple/{categoryId}")
    List<IngredientDto> getSimpleIngredientByCategoryId(
            @PathVariable("categoryId") Integer categoryId
    );
}
