package com.example.recipe_step_service.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeStepUpdateRequestDto {
    private Integer stepNumber;

    private String description;

    private String imgUrl;
}
