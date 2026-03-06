package com.example.Ingredients_service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientDto {
    @NotBlank(message = "category name must not be blank")
    private String name;
    private List<Integer> categoriesId;
}
