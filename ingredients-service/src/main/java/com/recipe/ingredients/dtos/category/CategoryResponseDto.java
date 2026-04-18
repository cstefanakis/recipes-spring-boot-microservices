package com.recipe.ingredients.dtos.category;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDto {

    private Integer id;
    private String name;
    private String imgUrl;
}
