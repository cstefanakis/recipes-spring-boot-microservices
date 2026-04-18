package com.recipe.auth.controllers;

import com.recipe.auth.dto.*;
import com.recipe.auth.models.Role;
import com.recipe.auth.models.User;
import com.recipe.auth.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserDto> authenticatedUser() {

        CurrentUserDto currentUser = userService.getCurrentUser();

        return ResponseEntity.ok(currentUser);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> allUsers() {

        List <UserDto> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Integer userId){
        User user = userService.getUserById(userId);

        UserDto userDto = userService.toUserDto(user);

        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role/{userId}")
    public ResponseEntity<UserRequestIdAndRoleDto> updateRole(@PathVariable("userId") Integer userId,
                                                       @RequestParam("role") Role role){
        UserRequestIdAndRoleDto dto = userService.updatedUserRoleByUserId(userId, role);

        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<UpdateUserResponseDto> updatedUser(@PathVariable("userId") Integer userId,
                                                             @RequestBody UpdateUserRequestDto updateUserRequestDto){
        UpdateUserResponseDto updateDto = userService.getUpdatedUserByUserId(userId, updateUserRequestDto);
        return ResponseEntity.ok(updateDto);
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") Integer userId){
        userService.deleteUserById(userId);
    }
}