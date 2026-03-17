package com.example.recipe_step_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeStepCreateRequestDto {
    @NotNull
    private Integer stepNumber;

    @NotBlank
    private String description;

    private String imgUrl;

    @NotNull
    private Integer recipeId;
}
