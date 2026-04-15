package com.example.auth_service.services;

import com.example.auth_service.dtos.CurrentUserDto;
import com.example.auth_service.dtos.UserDto;
import com.example.auth_service.models.Role;
import com.example.auth_service.models.User;
import com.example.auth_service.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        //Mock
        doNothing().when(userRepository)
                .deleteById(userId);

        //Act
        userService.deleteUserById(userId);

        //Verify
        verify(userRepository)
                .deleteById(userId);
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
}