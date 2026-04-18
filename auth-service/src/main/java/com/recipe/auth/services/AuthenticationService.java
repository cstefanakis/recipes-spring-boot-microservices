package com.recipe.auth.services;

import com.recipe.auth.dto.*;
import com.recipe.auth.dto.*;
import com.recipe.auth.dto.*;
import com.recipe.auth.models.Role;
import com.recipe.auth.models.User;
import com.recipe.auth.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public CurrentUserDto signup(RegisterUserDto input) {

        validateEmailNotExists(input.getEmail());
        validateUsernameNotExists(input.getUsername());

        User user = User.builder()
                .fullName(input.getFullName())
                .username(input.getUsername())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        return userService.toCurrentUserDto(savedUser);
    }

    private void validateEmailNotExists(String email) {
        boolean isEmailExists = userRepository.isEmailExists(email);
        if(isEmailExists){
            throw new EntityExistsException(String.format("Email: %s already exists", email));
        }
    }

    private void validateUsernameNotExists(String username) {
        boolean isUsernameExists = userRepository.isUsernameExists(username);
        if(isUsernameExists){
            throw new EntityExistsException(String.format("Username: %s already exists", username));
        }
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

        return toUserDto(user);
    }

    public LoginResponse getAuthenticate(LoginUserDto loginUserDto) {

        User authenticatedUser = authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        return toLoginResponse(jwtToken, authenticatedUser);
    }

    private LoginResponse toLoginResponse(String jwtToken, User authenticatedUser) {

        UserDto userDto = toUserDto(authenticatedUser);

        return LoginResponse.builder()
                .token(jwtToken)
                .user(userDto)
                .role(authenticatedUser.getRole().name())
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }

    public UserRequestIdAndRoleDto getUserIdAndRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Integer userId = userService.getUserIdByEmailOrUsername(username);
        String userRole = userService.getUserRoleByEmailOrUsername(username);

        return new UserRequestIdAndRoleDto(userId, userRole);
    }

    private UserDto toUserDto(User user) {

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

}