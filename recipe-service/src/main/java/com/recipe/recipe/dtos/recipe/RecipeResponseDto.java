package com.recipe.recipe.dtos.recipe;

import com.recipe.recipe.dtos.category.CategoryResponseDto;
import com.recipe.recipe.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.recipe.recipe.dtos.recipeStep.RecipeStepResponseDto;
import com.recipe.recipe.dtos.user.UserResponseIdAndUsernameDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeResponseDto {
    private Integer id;

    private String title;

    private String description;

    private String imgUrl;

    private UserResponseIdAndUsernameDto author;

    @Builder.Default
    private List<RecipeIngredientResponseDto> ingredients = new ArrayList<>();

    @Builder.Default
    private List<RecipeStepResponseDto> recipeSteps = new ArrayList<>();

    @Builder.Default
    private List<CategoryResponseDto> categories = new ArrayList<>();
}
