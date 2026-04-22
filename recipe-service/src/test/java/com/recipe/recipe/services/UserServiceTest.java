package com.recipe.recipe.services;

import com.recipe.recipe.clients.UserClient;
import com.recipe.recipe.dtos.user.UserResponseIdAndRole;
import com.recipe.recipe.dtos.user.UserResponseIdAndUsernameDto;
import com.recipe.recipe.models.Category;
import com.recipe.recipe.models.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserClient userClient;

    private UserResponseIdAndRole user;
    private Recipe recipe;

    @BeforeEach
    void setup(){
        this.user = UserResponseIdAndRole.builder()
                .id(2)
                .role("USER")
                .build();

        Category category = Category.builder()
                .id(1)
                .name("Category")
                .imgUrl("url")
                .build();

        this.recipe = Recipe.builder()
                .id(1)
                .title("title")
                .description("description")
                .imgUrl("url")
                .categories(List.of(category))
                .userId(2)
                .build();

    }

    @Test
    void isOwnerOrAdmin_isOwner() {
        //Arrange
        Integer recipeOwnerId = this.recipe.getUserId();

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
        Integer recipeOwnerId = this.recipe.getUserId();
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
        Integer recipeOwnerId = this.recipe.getUserId();
        UserResponseIdAndRole user = UserResponseIdAndRole.builder()
                .role("USER")
                .id(3)
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

    @Test
    void getUserIdAndUsernameByUserId() {
        //Arrange
        Integer userId = this.user.getId();

        UserResponseIdAndUsernameDto dto = UserResponseIdAndUsernameDto.builder()
                .id(userId)
                .username("username")
                .build();

        ResponseEntity<UserResponseIdAndUsernameDto> response = ResponseEntity.ok(dto);

        //Mock
        when(userClient.getUserIdAndUsernameByUserId(userId))
                .thenReturn(response);

        //Act
        UserResponseIdAndUsernameDto result =
                userService.getUserIdAndUsernameByUserId(userId);

        //Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getUsername(), result.getUsername());

        //Verify
        verify(userClient, times(1))
                .getUserIdAndUsernameByUserId(userId);
    }
}