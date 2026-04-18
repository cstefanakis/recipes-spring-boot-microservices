package com.recipe.ingredients.dtos.ingredient;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientSimpleResponseDto {
    private Integer id;
    private String name;
    private String imgUrl;
}
