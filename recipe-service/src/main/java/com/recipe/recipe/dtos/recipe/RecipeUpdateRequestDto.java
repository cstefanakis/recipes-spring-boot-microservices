package com.recipe.recipe.dtos.recipe;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeUpdateRequestDto {

    private String title;

    private String description;

    @URL(message = "Invalid image URL")
    private String imgUrl;

    private List<Integer> categoriesId;

    private Integer authorId;
}
