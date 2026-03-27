package com.example.recipe_service.dtos.category;

import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryUpdateRequestDto {
    private String name;

    @URL(message = "Invalid image URL")
    private String imgUrl;
}
