package com.example.auth_service.controllers;

import com.example.auth_service.dtos.CurrentUserDto;
import com.example.auth_service.dtos.UserDto;
import com.example.auth_service.dtos.UserRequestIdAndRoleDto;
import com.example.auth_service.models.Role;
import com.example.auth_service.models.User;
import com.example.auth_service.security.JwtAuthenticationFilter;
import com.example.auth_service.services.JwtService;
import com.example.auth_service.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User user;
    private UserDto userDto;

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

        this.userDto = UserDto.builder()
                .fullName(this.user.getFullName())
                .email(this.user.getEmail())
                .username(this.user.getUsername())
                .id(this.user.getId())
                .build();

    }

    @Test
    void authenticatedUser() throws Exception {
        //Arrange
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
        when(userService.getCurrentUser())
                .thenReturn(currentUserDto);

        //Perform get
        mockMvc.perform(get("/users/me"))
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
        verify(userService, times(1))
                .getCurrentUser();
    }

    @Test
    void allUsers() throws Exception {
        //Arrange
        List<UserDto> users = List.of(userDto);

        //Mock
        when(userService.allUsers())
                .thenReturn(users);

        //Perform get
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(status().isOk());

    }

    @Test
    void getUserById() throws Exception {
        //Arrange
        Integer userId = this.user.getId();

        //Mock
        when(userService.getUserById(userId))
                .thenReturn(this.user);
        when(userService.toUserDto(this.user))
                .thenReturn(this.userDto);

        //Perform get
        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(this.userDto.getId()))
                .andExpect(jsonPath("$.fullName").value(this.userDto.getFullName()))
                .andExpect(jsonPath("$.email").value(this.userDto.getEmail()))
                .andExpect(jsonPath("$.username").value(this.userDto.getUsername()));

        //Verify
        verify(userService, times(1))
                .getUserById(userId);
        verify(userService, times(1))
                .toUserDto(this.user);
    }

    @Test
    void deleteUser() throws Exception {
        //Arrange
        Integer userId = this.user.getId();

        //Mock
        doNothing().when(userService)
                .deleteUserById(userId);

        //Perform Delete
        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isNoContent());

        //Verify
        verify(userService, times(1))
                .deleteUserById(userId);
    }

    @Test
    void updateRole() throws Exception {
        //Arrange
        Integer userId = this.user.getId();
        Role role = Role.ADMIN;
        UserRequestIdAndRoleDto userRequestIdAndRoleDto
                = UserRequestIdAndRoleDto.builder()
                .id(userId)
                .role(role.name())
                .build();

        //Mock
        when(userService.updatedUserRoleByUserId(userId, role))
                .thenReturn(userRequestIdAndRoleDto);

        //Perform put
        mockMvc.perform(put("/users/role/{userId}", userId)
                .param("role",role.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.role").value(role.name()));

        //Verify
        verify(userService, times(1))
                .updatedUserRoleByUserId(userId, role);
    }
}