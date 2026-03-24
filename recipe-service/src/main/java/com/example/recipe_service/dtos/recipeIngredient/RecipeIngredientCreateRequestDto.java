package com.example.recipe_service.dtos.recipeIngredient;

import com.example.recipe_service.enums.Unit;
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
