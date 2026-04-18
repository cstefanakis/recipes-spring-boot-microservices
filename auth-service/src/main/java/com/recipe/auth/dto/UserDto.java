package com.recipe.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Integer id;
    private String fullName;
    private String email;
    private String username;
    private String role;
}
