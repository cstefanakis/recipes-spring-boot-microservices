package com.recipe.recipe.services;

import com.recipe.recipe.clients.UserClient;
import com.recipe.recipe.dtos.user.UserResponseIdAndRole;
import com.recipe.recipe.dtos.user.UserResponseIdAndUsernameDto;
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

    public UserResponseIdAndUsernameDto getUserIdAndUsernameByUserId(Integer userId) {
        return userClient.getUserIdAndUsernameByUserId(userId).getBody();
    }
}
