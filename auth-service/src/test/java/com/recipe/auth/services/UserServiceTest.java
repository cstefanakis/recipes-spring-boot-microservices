package com.recipe.auth.services;

import com.recipe.auth.dto.*;
import com.recipe.auth.models.Role;
import com.recipe.auth.models.User;
import com.recipe.auth.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user01;
    private User admin;

    @BeforeEach
    void setup(){

        this.user01 = User.builder()
                .id(1)
                .username("user")
                .email("user@test.com")
                .password("pass")
                .fullName("test user")
                .role(Role.USER)
                .build();

        this.admin = User.builder()
                .id(2)
                .username("admin")
                .email("admin@test.com")
                .password("pass02")
                .fullName("test admin")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void allUsers() {
        //Arrange
        List<User> users = List.of(this.user01, this.admin);

        //Mock
        when(userRepository.findAll())
                .thenReturn(users);

        //Act
        List<UserDto> result = userService.allUsers();

        //Assert
        assertNotNull(result);
        assertTrue(result.stream()
                .anyMatch(uDto -> uDto.getFullName().equals(this.user01.getFullName())));
        assertTrue(result.stream()
                .anyMatch(uDto -> uDto.getEmail().equals(this.user01.getEmail())));
        assertTrue(result.stream()
                .anyMatch(uDto -> uDto.getUsername().equals(this.user01.getUsername())));
        assertTrue(result.stream()
                .anyMatch(uDto -> uDto.getId().equals(this.user01.getId())));

        //Verify
        verify(userRepository, times(1))
                .findAll();
    }

    @Test
    void deleteUserById() {
        //Arrange
        Integer userId = this.user01.getId();
        String role = this.user01.getRole().name();
        String username = this.user01.getUsername();

        //Mock
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findUserRoleByEmailOrUsername(username))
                .thenReturn(role);
        when(userRepository.findUserIdByEmailOrUsername(username))
                .thenReturn(userId);
        doNothing().when(userRepository)
                .deleteById(userId);

        //Act
        userService.deleteUserById(userId);

        //Verify
        verify(userRepository, times(1))
                .findUserRoleByEmailOrUsername(username);
        verify(userRepository, times(1))
                .findUserIdByEmailOrUsername(any(String.class));
        verify(userRepository)
                .deleteById(userId);
    }

    @Test
    void deleteUserById_notPermission() {
        //Arrange
        Integer ownerUserId = this.user01.getId();
        String ownerRole = this.user01.getRole().name();
        String ownerUsername = this.user01.getUsername();
        Integer otherUserId = 2;

        //Mock
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(ownerUsername, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findUserRoleByEmailOrUsername(ownerUsername))
                .thenReturn(ownerRole);
        when(userRepository.findUserIdByEmailOrUsername(ownerUsername))
                .thenReturn(otherUserId);

        //Act
        assertThrows(RuntimeException.class,
                () -> userService.deleteUserById(ownerUserId));

        //Verify
        verify(userRepository, times(1))
                .findUserRoleByEmailOrUsername(ownerUsername);
        verify(userRepository, times(1))
                .findUserIdByEmailOrUsername(ownerUsername);
    }

    @Test
    void getUserByEmailOrUsername_byUsername() {
        //Arrange
        String username = this.user01.getUsername();

        //Mock
        when(userRepository.findByEmailOrUsername(username))
                .thenReturn(Optional.of(this.user01));

        //Act
        User result = userService.getUserByEmailOrUsername(username);

        //Assert
        assertNotNull(result);
        assertEquals(this.user01, result);

        //Verify
        verify(userRepository, times(1))
                .findByEmailOrUsername(username);
    }

    @Test
    void getUserByEmailOrUsername_byEmail() {
        //Arrange
        String email = this.admin.getEmail();

        //Mock
        when(userRepository.findByEmailOrUsername(email))
                .thenReturn(Optional.of(this.admin));

        //Act
        User result = userService.getUserByEmailOrUsername(email);

        //Assert
        assertNotNull(result);
        assertEquals(this.admin, result);

        //Verify
        verify(userRepository, times(1))
                .findByEmailOrUsername(email);
    }

    @Test
    void toUserDto() {
        //Act
        UserDto result = userService.toUserDto(this.user01);

        //Assert
        assertNotNull(result);
        assertEquals(this.user01.getEmail(), result.getEmail());
        assertEquals(this.user01.getUsername(), result.getUsername());
        assertEquals(this.user01.getFullName(), result.getFullName());
        assertEquals(this.user01.getId(), result.getId());
    }

    @Test
    void getUserIdByEmailOrUsername() {
        //Arrange
        String username = this.user01.getUsername();
        Integer userId = this.user01.getId();

        //Mock
        when(userRepository.findUserIdByEmailOrUsername(username))
                .thenReturn(userId);

        //Act
        Integer result = userService.getUserIdByEmailOrUsername(username);

        //Assert
        assertNotNull(result);
        assertEquals(userId, result);

        //Verify
        verify(userRepository, times(1))
                .findUserIdByEmailOrUsername(username);
    }

    @Test
    void getUserRoleByEmailOrUsername() {
        //Arrange
        String username = this.user01.getUsername();
        String userRole = this.user01.getRole().name();

        //Mock
        when(userRepository.findUserRoleByEmailOrUsername(username))
                .thenReturn(userRole);

        //Act
        String result = userService.getUserRoleByEmailOrUsername(username);

        //Assert
        assertNotNull(result);
        assertEquals(userRole, result);

        //Verify
        verify(userRepository, times(1))
                .findUserRoleByEmailOrUsername(username);
    }

    @Test
    void getCurrentUser() {
        //Arrange
        String username = this.user01.getUsername();

        //Mock
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmailOrUsername(username))
                .thenReturn(Optional.of(this.user01));

        //Act
        CurrentUserDto result = userService.getCurrentUser();

        //Assert
        assertNotNull(result);
        assertEquals(this.user01.getUsername(), result.getUsername());
        assertEquals(this.user01.getRole(), result.getRole());
        assertEquals(this.user01.getEmail(), result.getEmail());
        assertEquals(this.user01.getFullName(), result.getFullName());

        //verify
        verify(userRepository, times(1))
                .findByEmailOrUsername(username);
    }

    @Test
    void updatedUserRoleByUserId() {
        //Arrange
        Integer userId = this.user01.getId();
        Role role = Role.ADMIN;
        this.user01.setRole(role);

        //Mock
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(this.user01));
        when(userRepository.save(any(User.class)))
                .thenReturn(this.user01);

        //Act
        UserRequestIdAndRoleDto result = userService.updatedUserRoleByUserId(userId, role);

        //Assert
        assertNotNull(result);
        assertEquals(role.name(), result.getRole());

        //Verify
        verify(userRepository, times(1))
                .findById(userId);
        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void getUpdatedUserByUserId() {
        //Arrange
        Integer currentUserId = this.user01.getId();

        String username = this.user01.getUsername();

        String currentRole = this.user01.getRole().name();

        UpdateUserRequestDto updateUserRequestDto =
                UpdateUserRequestDto.builder()
                        .fullName("updatedFullName")
                        .username("updatedUsername")
                        .email("updateUser@test.com")
                        .password("newPassword")
                        .build();

        //Mock
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findUserRoleByEmailOrUsername(username))
                .thenReturn(currentRole);
        when(userRepository.findUserIdByEmailOrUsername(username))
                .thenReturn(currentUserId);
        when(userRepository.findById(currentUserId))
                .thenReturn(Optional.of(this.user01));

        //Act
        UpdateUserResponseDto result =
                userService.getUpdatedUserByUserId(currentUserId, updateUserRequestDto);

        //Assert
        assertNotNull(result);
        assertEquals(updateUserRequestDto.getUsername(), result.getUsername());
        assertEquals(updateUserRequestDto.getFullName(), result.getFullName());
        assertEquals(updateUserRequestDto.getEmail(), result.getEmail());

        //Verify
        verify(userRepository, times(1))
                .findUserRoleByEmailOrUsername(username);
        verify(userRepository, times(1))
                .findUserIdByEmailOrUsername(username);
        verify(userRepository, times(1))
                .findById(currentUserId);
    }

    @Test
    void getUpdatedUserByUserId_NullFullName() {
        //Arrange
        Integer currentUserId = this.user01.getId();

        String username = this.user01.getUsername();

        String currentRole = this.user01.getRole().name();

        UpdateUserRequestDto updateUserRequestDto =
                UpdateUserRequestDto.builder()
                        .username("updatedUsername")
                        .email("updateUser@test.com")
                        .password("newPassword")
                        .build();

        //Mock
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findUserRoleByEmailOrUsername(username))
                .thenReturn(currentRole);
        when(userRepository.findUserIdByEmailOrUsername(username))
                .thenReturn(currentUserId);
        when(userRepository.findById(currentUserId))
                .thenReturn(Optional.of(this.user01));

        //Act
        UpdateUserResponseDto result =
                userService.getUpdatedUserByUserId(currentUserId, updateUserRequestDto);

        //Assert
        assertNotNull(result);
        assertEquals(updateUserRequestDto.getUsername(), result.getUsername());
        assertEquals(this.user01.getFullName(), result.getFullName());
        assertEquals(updateUserRequestDto.getEmail(), result.getEmail());

        //Verify
        verify(userRepository, times(1))
                .findUserRoleByEmailOrUsername(username);
        verify(userRepository, times(1))
                .findUserIdByEmailOrUsername(username);
        verify(userRepository, times(1))
                .findById(currentUserId);
    }

    @Test
    void getUpdatedUserByUserId_NullUsername() {
        //Arrange
        Integer currentUserId = this.user01.getId();

        String username = this.user01.getUsername();

        String currentRole = this.user01.getRole().name();

        UpdateUserRequestDto updateUserRequestDto =
                UpdateUserRequestDto.builder()
                        .fullName("updatedFullName")
                        .email("updateUser@test.com")
                        .password("newPassword")
                        .build();

        //Mock
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findUserRoleByEmailOrUsername(username))
                .thenReturn(currentRole);
        when(userRepository.findUserIdByEmailOrUsername(username))
                .thenReturn(currentUserId);
        when(userRepository.findById(currentUserId))
                .thenReturn(Optional.of(this.user01));

        //Act
        UpdateUserResponseDto result =
                userService.getUpdatedUserByUserId(currentUserId, updateUserRequestDto);

        //Assert
        assertNotNull(result);
        assertEquals(this.user01.getUsername(), result.getUsername());
        assertEquals(updateUserRequestDto.getFullName(), result.getFullName());
        assertEquals(updateUserRequestDto.getEmail(), result.getEmail());

        //Verify
        verify(userRepository, times(1))
                .findUserRoleByEmailOrUsername(username);
        verify(userRepository, times(1))
                .findUserIdByEmailOrUsername(username);
        verify(userRepository, times(1))
                .findById(currentUserId);
    }

    @Test
    void getUpdatedUserByUserId_NullEmail() {
        //Arrange
        Integer currentUserId = this.user01.getId();

        String username = this.user01.getUsername();

        String currentRole = this.user01.getRole().name();

        UpdateUserRequestDto updateUserRequestDto =
                UpdateUserRequestDto.builder()
                        .fullName("updatedFullName")
                        .username("updatedUsername")
                        .password("newPassword")
                        .build();

        //Mock
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findUserRoleByEmailOrUsername(username))
                .thenReturn(currentRole);
        when(userRepository.findUserIdByEmailOrUsername(username))
                .thenReturn(currentUserId);
        when(userRepository.findById(currentUserId))
                .thenReturn(Optional.of(this.user01));

        //Act
        UpdateUserResponseDto result =
                userService.getUpdatedUserByUserId(currentUserId, updateUserRequestDto);

        //Assert
        assertNotNull(result);
        assertEquals(updateUserRequestDto.getUsername(), result.getUsername());
        assertEquals(updateUserRequestDto.getFullName(), result.getFullName());
        assertEquals(this.user01.getEmail(), result.getEmail());

        //Verify
        verify(userRepository, times(1))
                .findUserRoleByEmailOrUsername(username);
        verify(userRepository, times(1))
                .findUserIdByEmailOrUsername(username);
        verify(userRepository, times(1))
                .findById(currentUserId);
    }
}