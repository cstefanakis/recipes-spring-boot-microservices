package com.example.recipe_step_service.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeStepResponseDto {
    private Integer recipeStepId;
    private Integer stepNumber;
    private String description;
    private String imgUrl;
    private Integer recipeId;
}
