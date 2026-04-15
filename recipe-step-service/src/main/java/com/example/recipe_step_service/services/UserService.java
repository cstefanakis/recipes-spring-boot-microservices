package com.example.recipe_step_service.services;

import com.example.recipe_step_service.clients.UserClient;
import com.example.recipe_step_service.dtos.user.UserResponseIdAndRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserClient userClient;

    public boolean isOwnerOrAdmin(Integer recipeOwnerId) {

        UserResponseIdAndRole authenticatedUser = userClient.authenticatedUserIdAndRole().getBody();

        return authenticatedUser.getId().equals(recipeOwnerId)
                || authenticatedUser.getRole().equals("ADMIN");
    }

    public UserResponseIdAndRole getAuthenticatedUser(){
        return userClient.authenticatedUserIdAndRole().getBody();
    }
}
