package com.recipe.recipe.dtos.recipeIngredient;

import com.recipe.recipe.enums.Unit;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeIngredientCreateRequestDto {

    @NotNull
    private Integer ingredientId;

    @NotNull
    private Unit unit;

    @NotNull
    private Double quantity;

    @NotNull
    private Integer recipeId;
}
