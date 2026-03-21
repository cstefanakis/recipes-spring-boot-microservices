package com.example.Ingredients_service.dtos.ingredient;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientCreateRequestDto {

    @NotBlank(message = "category name must not be blank")
    private String name;

    private String imgUrl;

    private List<Integer> categoriesId;
}
