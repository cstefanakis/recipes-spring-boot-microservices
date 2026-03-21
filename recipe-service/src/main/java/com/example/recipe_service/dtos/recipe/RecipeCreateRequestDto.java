package com.example.recipe_service.dtos.recipe;

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

    @NotNull
    private List<Integer> categoriesId;
}
