package com.example.Ingredients_service.dtos.ingredient;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientUpdateRequestDto {
    private String name;
    private String imgUrl;
    private List<Integer> categoriesId;
}
