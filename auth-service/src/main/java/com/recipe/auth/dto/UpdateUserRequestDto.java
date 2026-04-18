package com.recipe.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDto {
    private String fullName;
    private String username;
    private String email;
    private String password;
}
