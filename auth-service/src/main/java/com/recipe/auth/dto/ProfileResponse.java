package com.recipe.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String username;
    private long expiresIn;
}
