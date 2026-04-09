package com.example.recipe_service.dtos.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Integer id;
    private String fullName;
    private String email;
    private String username;
    private String role;
}
