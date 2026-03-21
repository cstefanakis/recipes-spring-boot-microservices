package com.example.recipe_service.dtos.recipe;

import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.example.recipe_service.dtos.recipeStep.RecipeStepResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeResponseDto {
    private String title;
    private String description;
    private String imgUrl;
    @Builder.Default
    private List<RecipeIngredientResponseDto> ingredients = new ArrayList<>();
    @Builder.Default
    private List<RecipeStepResponseDto> recipeSteps = new ArrayList<>();
    @Builder.Default
    private List<CategoryResponseDto> categories = new ArrayList<>();
}
