package com.example.recipe_service.dtos.recipe;

import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.dtos.ingredient.IngredientGlobalResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeGlobalResponseDto {
    private String title;
    private String description;
    private String imgUrl;
    @Builder.Default
    private List<IngredientGlobalResponseDto> ingredients = new ArrayList<>();
    @Builder.Default
    private List<CategoryResponseDto> categories = new ArrayList<>();
}
