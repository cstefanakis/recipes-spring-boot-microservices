package com.example.Ingredients_service.dtos.ingredient;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientUpdateRequestDto {
    private String name;

    @URL(message = "Invalid image URL")
    private String imgUrl;

    private List<Integer> categoriesId;
}
