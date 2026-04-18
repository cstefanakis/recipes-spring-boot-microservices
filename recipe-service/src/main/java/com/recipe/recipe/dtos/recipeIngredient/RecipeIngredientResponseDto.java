package com.recipe.recipe.dtos.recipeIngredient;

import com.recipe.recipe.enums.Unit;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeIngredientResponseDto {
    private Integer id;
    private String name;
    private Double quantity;
    private Unit unit;
    private String imgUrl;
}
