package com.example.recipe_service.dtos.recipeStep;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeStepCreateRequestDto {

    private Integer stepNumber;

    private String description;

    private String imgUrl;

    private Integer recipeId;
}
