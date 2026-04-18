package com.recipe.recipestep.dtos.recipeStep;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeStepResponseDto {
    private Integer id;
    private Integer stepNumber;
    private String description;
    private String imgUrl;
    private Integer recipeId;
}
