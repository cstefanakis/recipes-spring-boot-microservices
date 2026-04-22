package com.recipe.recipe.dtos.recipe;

import com.recipe.recipe.dtos.user.UserResponseIdAndUsernameDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeSimpleResponseDto {
    private Integer id;
    private String title;
    private String description;
    private String imgUrl;
    private UserResponseIdAndUsernameDto author;
}
