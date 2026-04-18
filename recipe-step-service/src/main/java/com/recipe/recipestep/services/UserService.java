package com.recipe.recipestep.services;

import com.recipe.recipestep.clients.UserClient;
import com.recipe.recipestep.dtos.user.UserResponseIdAndRole;
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
