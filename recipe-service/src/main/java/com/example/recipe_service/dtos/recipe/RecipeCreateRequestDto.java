package com.example.recipe_service.dtos.recipe;

import com.example.recipe_service.dtos.ingredient.IngredientRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeCreateRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String imgUrl;

    private List<RecipeCreateRequestDto> recipeSteps;

    @NotNull
    private List<IngredientRequestDto> ingredients;

    @NotNull
    private List<Integer> categoriesId;
}
