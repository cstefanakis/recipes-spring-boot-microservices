package com.recipe.recipestep.dtos.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseIdAndRole {
    private Integer id;
    private String role;
}
