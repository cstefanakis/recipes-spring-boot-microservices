package com.recipe.auth.services;

import com.recipe.auth.dto.*;
import com.recipe.auth.models.Role;
import com.recipe.auth.models.User;
import com.recipe.auth.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    private User user;
    private LoginUserDto loginUser;

    @BeforeEach
    void setup(){

        this.user = User.builder()
                .id(1)
                .role(Role.USER)
                .email("test@test.com")
                .username("test")
                .fullName("test name")
                .password("superSecretPassword")
                .build();

        this.loginUser = LoginUserDto.builder()
                .emailOrUsername("test")
                .password("123456")
                .build();

    }

    @Test
    void signup() {
        //Arrange
        RegisterUserDto input = RegisterUserDto.builder()
                .username("test")
                .fullName("test name")
                .email("test@test.com")
                .password("123456")
                .build();

        CurrentUserDto currentUserDto =
                CurrentUserDto.builder()
                        .fullName(this.user.getFullName())
                        .username(this.user.getUsername())
                        .email(this.user.getEmail())
                        .role(this.user.getRole())
                        .createdAt(this.user.getCreatedAt())
                        .updatedAt(this.user.getUpdatedAt())
                        .isAccountNonExpired(this.user.isAccountNonExpired())
                        .isAccountNonLocked(this.user.isAccountNonLocked())
                        .isCredentialsNonExpired(this.user.isCredentialsNonExpired())
                        .isEnabled(this.user.isEnabled())
                        .build();


        //Mock
        when(userRepository.isUsernameExists(input.getUsername()))
                .thenReturn(false);
        when(userRepository.isEmailExists(input.getEmail()))
                .thenReturn(false);
        when(passwordEncoder.encode(input.getPassword()))
                .thenReturn("superSecretPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(userService.toCurrentUserDto(this.user))
                .thenReturn(currentUserDto);

        //Act
        CurrentUserDto result = authenticationService.signup(input);

        //Assert
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getFullName(), result.getFullName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRole(), result.getRole());

        //Verify
        verify(userRepository, times(1))
                .isUsernameExists(input.getUsername());
        verify(userRepository, times(1))
                .isUsernameExists(input.getUsername());
        verify(passwordEncoder, times(1))
                .encode(input.getPassword());
        verify(userRepository, times(1))
                .save(any(User.class));
        verify(userService, times(1))
                .toCurrentUserDto(this.user);
    }

    @Test
    void authenticate() {
        //Arrange
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(this.loginUser.getEmailOrUsername(),
                        this.loginUser.getPassword());

        //Mock
        when(userRepository.findByEmailOrUsername(this.loginUser.getEmailOrUsername()))
                .thenReturn(Optional.of(this.user));

        //Act
        User result = authenticationService.authenticate(loginUser);

        //Assert
        assertNotNull(result);
        assertEquals(this.user, result);

        //Verify
        verify(authenticationManager, times(1))
                .authenticate(usernamePasswordAuthenticationToken);
        verify(userRepository, times(1))
                .findByEmailOrUsername(this.loginUser.getEmailOrUsername());
    }

    @Test
    void getLoginUser() {
        //Arrange
        String username = this.user.getUsername();

        //Mock
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.getUserByEmailOrUsername(username))
                .thenReturn(this.user);

        //Act
        UserDto result = authenticationService.getLoginUser();

        //Assert
        assertNotNull(result);
        assertEquals(this.user.getUsername(), result.getUsername());
        assertEquals(this.user.getEmail(), result.getEmail());
        assertEquals(this.user.getFullName(), result.getFullName());

        //Verify
        verify(userService, times(1))
                .getUserByEmailOrUsername(username);
    }

    @Test
    void getAuthenticate() {
        //Arrange
        String jwtToken = "strongSecurityJwtToken";
        Long expirationTime = 28000L;
        String usernameOrEmail = this.loginUser.getEmailOrUsername();

        //Mock
        when(userRepository.findByEmailOrUsername(usernameOrEmail))
                .thenReturn(Optional.of(this.user));
        when(jwtService.generateToken(this.user))
                .thenReturn(jwtToken);
        when(jwtService.getExpirationTime())
                .thenReturn(expirationTime);

        //Act
        LoginResponse result =
                authenticationService.getAuthenticate(this.loginUser);

        //Assert
        assertNotNull(result);
        assertEquals(jwtToken, result.getToken());
        assertEquals(this.user.getUsername(), result.getUser().getUsername());
        assertEquals(this.user.getEmail(), result.getUser().getEmail());
        assertEquals(this.user.getFullName(), result.getUser().getFullName());
        assertEquals(this.user.getRole().name(), result.getRole());
        assertEquals(expirationTime, result.getExpiresIn());

        //Verify
        verify(userRepository, times(1))
                .findByEmailOrUsername(usernameOrEmail);
        verify(jwtService, times(1))
                .generateToken(this.user);
        verify(jwtService, times(1))
                .getExpirationTime();
    }

    @Test
    void getUserIdAndRole() {
        //Arrange
        String username = this.user.getUsername();
        Integer userId = this.user.getId();
        String userRole = this.user.getRole().name();

        //Mock
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.getUserIdByEmailOrUsername(username))
                .thenReturn(userId);
        when(userService.getUserRoleByEmailOrUsername(username))
                .thenReturn(userRole);

        //Act
        UserRequestIdAndRoleDto result = authenticationService.getUserIdAndRole();

        //Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(userRole, result.getRole());

        //Verify
        verify(userService, times(1))
                .getUserIdByEmailOrUsername(username);
        verify(userService, times(1))
                .getUserRoleByEmailOrUsername(username);
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }
}