package com.example.auth_service.services;

import com.example.auth_service.dtos.*;
import com.example.auth_service.models.Role;
import com.example.auth_service.models.User;
import com.example.auth_service.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtService jwtService;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, UserService userService, JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public User signup(RegisterUserDto input) {
        User user = User.builder()
                .fullName(input.getFullName())
                .username(input.getUsername())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmailOrUsername(),
                        input.getPassword()
                )
        );

        User user = userRepository.findByEmailOrUsername(input.getEmailOrUsername())
                .orElseThrow();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());

        return user;
    }

    public UserDto getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userService.getUserByEmailOrUsername(username);

        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    public LoginResponse getAuthenticate(LoginUserDto loginUserDto) {
        User authenticatedUser = authenticate(loginUserDto);

        UserDto user = UserDto.builder()
                .email(authenticatedUser.getEmail())
                .fullName(authenticatedUser.getFullName())
                .username(authenticatedUser.getUsername())
                .build();

        String jwtToken = jwtService.generateToken(authenticatedUser);

        return LoginResponse.builder()
                .token(jwtToken)
                .user(user)
                .role(authenticatedUser.getRole().name())
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }

    public UserRequestIdAndRoleDto getUserIdAndRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Integer userId = userService.getUserIdByEmailOrUsername(username);
        String userRole = userService.getUserRoleByEmailOrUsername(username);

        return UserRequestIdAndRoleDto.builder()
                .id(userId)
                .role(userRole)
                .build();
    }
}