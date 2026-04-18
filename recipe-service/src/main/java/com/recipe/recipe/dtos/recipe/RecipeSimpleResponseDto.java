package com.recipe.recipe.dtos.recipe;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeSimpleResponseDto {
    private Integer id;
    private String title;
    private String description;
    private String imgUrl;
}
