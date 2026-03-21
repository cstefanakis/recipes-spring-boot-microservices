package com.example.recipe_service.dtos.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryCreateRequestDto {

    @NotBlank
    private String name;

    private String imgUrl;
}
