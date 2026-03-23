package com.example.recipe_service.dtos.recipeIngredient;

import com.example.recipe_service.enums.Unit;
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
