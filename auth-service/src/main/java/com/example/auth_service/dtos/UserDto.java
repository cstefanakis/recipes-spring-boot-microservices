package com.example.auth_service.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String fullName;
    private String email;
    private String userName;
}
