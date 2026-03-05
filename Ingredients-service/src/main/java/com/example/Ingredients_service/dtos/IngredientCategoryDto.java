package com.example.Ingredients_service.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientCategoryDto {
    private Integer id;
    private String name;
}
