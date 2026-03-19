package com.example.Ingredients_service.dtos.category;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryUpdateRequestDto {

    private String name;
    private String imgUrl;
}
