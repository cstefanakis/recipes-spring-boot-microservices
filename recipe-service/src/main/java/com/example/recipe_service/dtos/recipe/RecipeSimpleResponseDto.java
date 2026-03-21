package com.example.recipe_service.dtos.recipe;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeSimpleResponseDto {
    private String title;
    private String description;
    private String imgUrl;
}
