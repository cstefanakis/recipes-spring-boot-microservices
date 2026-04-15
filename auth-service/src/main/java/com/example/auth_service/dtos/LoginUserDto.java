package com.example.auth_service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {

    @NotBlank
    private String emailOrUsername;

    @NotBlank
    private String password;
}
