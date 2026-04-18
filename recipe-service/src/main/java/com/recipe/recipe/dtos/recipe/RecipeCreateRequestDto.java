package com.recipe.recipe.dtos.recipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

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

    @URL(message = "Invalid image URL")
    private String imgUrl;

    @NotNull
    private List<Integer> categoriesId;
}
