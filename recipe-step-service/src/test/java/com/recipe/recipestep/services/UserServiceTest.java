package com.recipe.recipestep.services;

import com.recipe.recipestep.clients.UserClient;
import com.recipe.recipestep.dtos.user.UserResponseIdAndRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserClient userClient;

    private UserResponseIdAndRole user;

    @BeforeEach
    void setup(){
        this.user = UserResponseIdAndRole.builder()
                .id(2)
                .role("USER")
                .build();

    }

    @Test
    void isOwnerOrAdmin_isOwner() {
        //Arrange
        Integer recipeOwnerId = 2;

        ResponseEntity<UserResponseIdAndRole> response = ResponseEntity.ok(this.user);

        //Mock
        when(userClient.authenticatedUserIdAndRole())
                .thenReturn(response);

        //Act
        boolean result = userService.isOwnerOrAdmin(recipeOwnerId);

        //Assert
        assertTrue(result);

        //Verify
        verify(userClient, times(1))
                .authenticatedUserIdAndRole();
    }

    @Test
    void isOwnerOrAdmin_isAdmin() {
        //Arrange
        Integer recipeOwnerId = 2;
        this.user.setRole("ADMIN");

        ResponseEntity<UserResponseIdAndRole> response = ResponseEntity.ok(this.user);

        //Mock
        when(userClient.authenticatedUserIdAndRole())
                .thenReturn(response);

        //Act
        boolean result = userService.isOwnerOrAdmin(recipeOwnerId);

        //Assert
        assertTrue(result);

        //Verify
        verify(userClient, times(1))
                .authenticatedUserIdAndRole();
    }

    @Test
    void isOwnerOrAdmin_isNotOwner() {
        //Arrange
        Integer recipeOwnerId = 3;
        UserResponseIdAndRole user = UserResponseIdAndRole.builder()
                .role("USER")
                .id(2)
                .build();

        ResponseEntity<UserResponseIdAndRole> response = ResponseEntity.ok(user);

        //Mock
        when(userClient.authenticatedUserIdAndRole())
                .thenReturn(response);

        //Act
        boolean result = userService.isOwnerOrAdmin(recipeOwnerId);

        //Assert
        assertFalse(result);

        //Verify
        verify(userClient, times(1))
                .authenticatedUserIdAndRole();
    }

    @Test
    void getAuthenticatedUser() {
        //Arrange
        ResponseEntity<UserResponseIdAndRole> response = ResponseEntity.ok(this.user);

        //Mock
        when(userClient.authenticatedUserIdAndRole())
                .thenReturn(response);

        //Act
        UserResponseIdAndRole result = userService.getAuthenticatedUser();

        //Assert
        assertNotNull(result);
        assertEquals(this.user, result);

        //Verify
        verify(userClient, times(1))
                .authenticatedUserIdAndRole();

    }
}