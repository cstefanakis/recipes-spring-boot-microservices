package com.example.recipe_service.dtos.ingredient;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientGlobalResponseDto {
    private String name;
    private String imgUrl;
}
