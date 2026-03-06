package com.example.ingredient_categories_service.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullResponseCategoryDto {
    private String name;
    private List<IngredientDto> ingredients;
}
