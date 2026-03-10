package com.example.recipe_service.dtos.ingredient;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientSimpleResponseDto {
    private String name;
    private String imgUrl;
}
