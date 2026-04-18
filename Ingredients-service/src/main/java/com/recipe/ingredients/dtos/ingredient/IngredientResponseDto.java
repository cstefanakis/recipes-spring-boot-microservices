package com.recipe.ingredients.dtos.ingredient;

import com.recipe.ingredients.dtos.category.CategoryResponseDto;
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
