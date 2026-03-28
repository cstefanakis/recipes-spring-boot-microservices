package com.example.recipe_service.dtos.recipeIngredient;

import com.example.recipe_service.enums.Unit;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeIngredientUpdateRequestDto {

    private Integer ingredientId;

    private Unit unit;

    private Double quantity;
}
