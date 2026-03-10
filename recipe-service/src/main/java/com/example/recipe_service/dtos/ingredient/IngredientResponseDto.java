package com.example.recipe_service.dtos.ingredient;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientResponseDto {
    private Integer name;
    private Double quantity;
    private String unit;
    private String imgUrl;
}
