package com.example.recipe_service.dtos.recipe;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeUpdateRequestDto {

    private String title;

    private String description;

    private String imgUrl;

    private List<Integer> categoriesId;
}
