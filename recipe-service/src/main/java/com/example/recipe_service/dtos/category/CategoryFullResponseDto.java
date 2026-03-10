package com.example.recipe_service.dtos.category;

import com.example.recipe_service.dtos.ingredient.IngredientSimpleResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryFullResponseDto {
    private String name;
    private String imgUrl;
    private List<IngredientSimpleResponseDto> ingredient;
}
