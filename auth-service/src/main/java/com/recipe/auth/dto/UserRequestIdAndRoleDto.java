package com.recipe.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestIdAndRoleDto {

    private Integer id;

    private String role;
}
