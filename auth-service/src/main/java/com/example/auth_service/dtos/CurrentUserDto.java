package com.example.auth_service.dtos;

import com.example.auth_service.models.Role;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserDto {
    private String fullName;
    private String username;
    private String email;
    private Role role;
    private Date createdAt;
    private Date updatedAt;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
}
