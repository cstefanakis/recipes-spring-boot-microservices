package com.example.Ingredients_service.dtos.ingredient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientCreateRequestDto {

    @NotBlank(message = "Ingredient name must not be blank")
    private String name;

    @URL(message = "Invalid image URL")
    private String imgUrl;

    @NotNull(message = "Categories must not be null")
    @NotEmpty(message = "At least one category must be provided")
    private List<Integer> categoriesId;
}
