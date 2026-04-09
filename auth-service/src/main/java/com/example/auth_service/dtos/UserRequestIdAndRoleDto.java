package com.example.auth_service.dtos;

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
