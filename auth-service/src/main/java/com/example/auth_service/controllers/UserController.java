package com.example.auth_service.controllers;

import com.example.auth_service.dtos.CurrentUserDto;
import com.example.auth_service.dtos.UserDto;
import com.example.auth_service.models.User;
import com.example.auth_service.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<UserDto>> allUsers() {

        List <UserDto> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Integer userId){
        User user = userService.getUserById(userId);

        UserDto userDto = userService.toUserDto(user);

        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") Integer userId){
        userService.deleteUserById(userId);
    }
}