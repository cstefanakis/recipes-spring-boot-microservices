package com.example.recipe_service.dtos.recipe;

import com.example.recipe_service.dtos.ingredient.IngredientRequestDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeUpdateRequestDto {

    private String title;

    private String description;

    private String imgUrl;

    private List<RecipeUpdateRequestDto> recipeSteps;

    private List<IngredientRequestDto> ingredients;

    private List<Integer> categoriesId;
}
