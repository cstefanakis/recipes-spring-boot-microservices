package com.example.recipe_service.dtos.recipe;

import com.example.recipe_service.dtos.ingredient.IngredientResponseDto;
import com.example.recipe_service.models.Category;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeFullResponseDto {
    private String title;
    private String description;
    private String imgUrl;
    private List<IngredientResponseDto> ingredients;
    private List<Category> categories;
}
