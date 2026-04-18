package com.recipe.auth.controllers;

import com.recipe.auth.dto.*;
import com.recipe.auth.services.AuthenticationService;
import com.recipe.auth.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CurrentUserDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {

        CurrentUserDto registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {

        LoginResponse loginResponse
                = authenticationService.getAuthenticate(loginUserDto);

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> authenticatedUser() {

        UserDto userDto =
                authenticationService.getLoginUser();

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/profile-id-role")
    public ResponseEntity<UserRequestIdAndRoleDto> authenticatedUserIdAndRole() {

        UserRequestIdAndRoleDto dto =
                authenticationService.getUserIdAndRole();

        return ResponseEntity.ok(dto);
    }
}