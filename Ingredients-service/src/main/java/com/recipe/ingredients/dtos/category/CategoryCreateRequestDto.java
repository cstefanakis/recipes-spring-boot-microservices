package com.recipe.ingredients.dtos.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryCreateRequestDto {

    @NotBlank
    private String name;

    @URL(message = "Invalid image URL")
    private String imgUrl;
}
