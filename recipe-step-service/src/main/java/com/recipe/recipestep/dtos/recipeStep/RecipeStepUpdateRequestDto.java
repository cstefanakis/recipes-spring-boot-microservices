package com.recipe.recipestep.dtos.recipeStep;

import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeStepUpdateRequestDto {
    private Integer stepNumber;

    private String description;

    @URL(message = "Invalid image URL")
    private String imgUrl;
}
