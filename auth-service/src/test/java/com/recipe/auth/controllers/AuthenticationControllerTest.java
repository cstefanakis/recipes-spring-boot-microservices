package com.recipe.auth.controllers;

import com.recipe.auth.dto.*;
import com.recipe.auth.models.Role;
import com.recipe.auth.models.User;
import com.recipe.auth.security.JwtAuthenticationFilter;
import com.recipe.auth.services.AuthenticationService;
import com.recipe.auth.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User user;
    private UserDto userDto;

    @BeforeEach()
    void setup(){

        this.user = User.builder()
                .id(1)
                .role(Role.USER)
                .email("test@test.com")
                .username("test")
                .fullName("test name")
                .password("superSecretPassword")
                .build();

        this.userDto = UserDto.builder()
                .fullName(this.user.getFullName())
                .email(this.user.getEmail())
                .username(this.user.getUsername())
                .id(this.user.getId())
                .build();
    }

    @Test
    void register() throws Exception {
        //Arrange
        String request = """
                {
                    "email" : "test@test.com",
                    "username" : "test",
                    "password" : "superSecretPassword1",
                    "fullName" : "test name"
                }
                """;

        CurrentUserDto currentUserDto = CurrentUserDto.builder()
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
        when(authenticationService.signup(any(RegisterUserDto.class)))
                .thenReturn(currentUserDto);

        //Perform Post
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(this.user.getFullName()))
                .andExpect(jsonPath("$.username").value(this.user.getUsername()))
                .andExpect(jsonPath("$.email").value(this.user.getEmail()))
                .andExpect(jsonPath("$.role").value(this.user.getRole().name()))
                .andExpect(jsonPath("$.createdAt").value(this.user.getCreatedAt()))
                .andExpect(jsonPath("$.updatedAt").value(this.user.getUpdatedAt()))
                .andExpect(jsonPath("$.accountNonExpired").value(this.user.isAccountNonExpired()))
                .andExpect(jsonPath("$.accountNonLocked").value(this.user.isAccountNonLocked()))
                .andExpect(jsonPath("$.credentialsNonExpired").value(this.user.isCredentialsNonExpired()))
                .andExpect(jsonPath("$.enabled").value(this.user.isEnabled()));

        //Verify
        verify(authenticationService, times(1))
                .signup(any(RegisterUserDto.class));
    }

    @Test
    void register_WithIncorrectPassword() throws Exception {
        //Arrange
        String request = """
                {
                    "email" : "test@test.com",
                    "username" : "test",
                    "password" : "1234",
                    "fullName" : "test name"
                }
                """;

        //Perform Post
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_WithIncorrectEmail() throws Exception {
        //Arrange
        String request = """
                {
                    "email" : "testTest.com",
                    "username" : "test",
                    "password" : "superSecretPassword1",
                    "fullName" : "test name"
                }
                """;

        //Perform Post
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_nullEmail() throws Exception {
        //Arrange
        String request = """
                {
                    "username" : "test",
                    "password" : "superSecretPassword",
                    "fullName" : "test name"
                }
                """;

        //Perform Post
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_nullUsername() throws Exception {
        //Arrange
        String request = """
                {
                    "email" : "test@test.com",
                    "password" : "superSecretPassword",
                    "fullName" : "test name"
                }
                """;

        //Perform Post
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_nullPassword() throws Exception {
        //Arrange
        String request = """
                {
                    "email" : "test@test.com",
                    "username" : "test",
                    "fullName" : "test name"
                }
                """;

        //Perform Post
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_nullFullName() throws Exception {
        //Arrange
        String request = """
                {
                    "email" : "test@test.com",
                    "username" : "test",
                    "password" : "superSecretPassword"
                }
                """;

        //Perform Post
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticate() throws Exception {
        //Arrange
        LoginResponse loginResponse =
                LoginResponse.builder()
                        .role(this.user.getRole().name())
                        .token("superSecretToken123")
                        .expiresIn(280000)
                        .user(this.userDto)
                        .build();

        String requestBody = """
                {
                    "emailOrUsername" : "test",
                    "password" : "superSecretPassword"
                }
                """;

        //Mock
        when(authenticationService.getAuthenticate(any(LoginUserDto.class)))
                .thenReturn(loginResponse);

        //Perform Post
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(loginResponse.getToken()))
                .andExpect(jsonPath("$.user.username").value(loginResponse.getUser().getUsername()))
                .andExpect(jsonPath("$.user.email").value(loginResponse.getUser().getEmail()))
                .andExpect(jsonPath("$.user.fullName").value(loginResponse.getUser().getFullName()))
                .andExpect(jsonPath("$.user.id").value(loginResponse.getUser().getId()))
                .andExpect(jsonPath("$.role").value(loginResponse.getRole()))
                .andExpect(jsonPath("$.expiresIn").value(loginResponse.getExpiresIn()));

        //Verify
        verify(authenticationService, times(1))
                .getAuthenticate(any(LoginUserDto.class));

    }

    @Test
    void authenticate_NullEmailAndUsername() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "password" : "superSecretPassword"
                }
                """;

        //Perform Post
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticate_NullPassword() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "emailOrUsername" : "test"
                }
                """;

        //Perform Post
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticatedUser() throws Exception {
        //Mock
        when(authenticationService.getLoginUser())
                .thenReturn(this.userDto);

        //Perform Get
        mockMvc.perform(get("/api/auth/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(this.user.getId()))
                .andExpect(jsonPath("$.fullName").value(this.user.getFullName()))
                .andExpect(jsonPath("$.email").value(this.user.getEmail()))
                .andExpect(jsonPath("$.username").value(this.user.getUsername()));

        //Verify
        verify(authenticationService, times(1))
                .getLoginUser();
    }

    @Test
    void authenticatedUserIdAndRole() throws Exception {
        //Arrange
        UserResponseIdAndRoleDto userRequestIdAndRoleDto =
                UserResponseIdAndRoleDto.builder()
                        .id(this.user.getId())
                        .role(this.user.getRole().name())
                        .build();

        //Mock
        when(authenticationService.getUserIdAndRole())
                .thenReturn(userRequestIdAndRoleDto);

        //Perform Get
        mockMvc.perform(get("/api/auth/profile-id-role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(this.user.getId()))
                .andExpect(jsonPath("$.role").value(this.user.getRole().name()));

        //Verify
        verify(authenticationService, times(1))
                .getUserIdAndRole();
    }
}