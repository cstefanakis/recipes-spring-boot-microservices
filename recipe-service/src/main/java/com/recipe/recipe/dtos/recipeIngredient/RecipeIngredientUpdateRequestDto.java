package com.recipe.recipe.dtos.recipeIngredient;

import com.recipe.recipe.enums.Unit;
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
