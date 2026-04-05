package com.example.auth_service.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {
    private String email;

    private String password;
}
