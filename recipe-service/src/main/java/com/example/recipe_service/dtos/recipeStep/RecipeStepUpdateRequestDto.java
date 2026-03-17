package com.example.recipe_service.dtos.recipeStep;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeStepUpdateRequestDto {
    private String description;

    private String imgUrl;
}
