package com.example.Ingredients_service.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientResponseDto {

    private String name;
    private List<IngredientCategoryDto> categories;
}
