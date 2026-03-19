package com.example.Ingredients_service.dtos.ingredient;

import com.example.Ingredients_service.dtos.category.CategoryResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientResponseDto {
    private Integer id;
    private String name;
    private String imgUrl;
    private List<CategoryResponseDto> categories;
}
