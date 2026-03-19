package com.example.Ingredients_service.dtos.ingredient;

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
