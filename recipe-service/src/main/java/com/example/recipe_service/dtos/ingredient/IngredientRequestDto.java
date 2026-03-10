package com.example.recipe_service.dtos.ingredient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientRequestDto {

    @NotNull
    private Integer ingredientId;

    @NotNull
    private Double quantity;

    @NotBlank
    private String unit;

    private String imgUrl;
}
