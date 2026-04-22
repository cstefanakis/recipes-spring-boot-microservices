package com.recipe.recipe.dtos.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseIdAndUsernameDto {

    private Integer id;

    private String username;
}
