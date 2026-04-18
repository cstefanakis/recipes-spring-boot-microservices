package com.recipe.auth.repositories;

import com.recipe.auth.models.Role;
import com.recipe.auth.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user01;

    @BeforeEach
    void setup(){
        this.user01 = userRepository.save(User.builder()
                .username("user")
                .email("user@test.com")
                .password("pass")
                .fullName("test user")
                .role(Role.USER)
                .build());
    }

    @Test
    void findByEmailOrUsername_ByEmail() {
        //Arrange
        String email = this.user01.getEmail();

        //Act
        Optional<User> result =
                userRepository.findByEmailOrUsername(email);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(this.user01, result.get());
    }

    @Test
    void findByEmailOrUsername_ByUser() {
        //Arrange
        String username = this.user01.getUsername();

        //Act
        Optional<User> result =
                userRepository.findByEmailOrUsername(username);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(this.user01, result.get());
    }

    @Test
    void findUserIdByEmailOrUsername_ByUsername() {
        //Arrange
        String username = this.user01.getUsername();

        //Act
        Integer result =
                userRepository.findUserIdByEmailOrUsername(username);

        //Assert
        assertNotNull(result);
        assertEquals(this.user01.getId(), result);
    }

    @Test
    void findUserIdByEmailOrUsername_ByEmail() {
        //Arrange
        String email = this.user01.getEmail();

        //Act
        Integer result =
                userRepository.findUserIdByEmailOrUsername(email);

        //Assert
        assertNotNull(result);
        assertEquals(this.user01.getId(), result);
    }

    @Test
    void findUserRoleByEmailOrUsername_ByEmail() {
        //Arrange
        String email = this.user01.getEmail();

        //Act
        String result = userRepository.findUserRoleByEmailOrUsername(email);

        //Assert
        assertNotNull(result);
        assertEquals(this.user01.getRole().name(), result);
    }

    @Test
    void findUserRoleByEmailOrUsername_ByUsername() {
        //Arrange
        String username = this.user01.getUsername();

        //Act
        String result = userRepository.findUserRoleByEmailOrUsername(username);

        //Assert
        assertNotNull(result);
        assertEquals(this.user01.getRole().name(), result);
    }

    @Test
    void isEmailExists_isExists() {
        //Arrange
        String email = this.user01.getEmail();

        //Act
        boolean result = userRepository.isEmailExists(email);

        //Assert
        assertTrue(result);
    }

    @Test
    void isEmailExists_isNotExists() {
        //Arrange
        String email = "noExist@email.com";

        //Act
        boolean result = userRepository.isEmailExists(email);

        //Assert
        assertFalse(result);
    }

    @Test
    void isUsernameExists_isExist() {
        //Arrange
        String username = this.user01.getUsername();

        //Act
        boolean result = userRepository.isUsernameExists(username);

        //Assert
        assertTrue(result);
    }

    @Test
    void isUsernameExists_isNotExist() {
        //Arrange
        String username = "notExistsUsername";

        //Act
        boolean result = userRepository.isUsernameExists(username);

        //Assert
        assertFalse(result);
    }

    @Test
    void findUserRoleByUserId() {
        //Arrange
        Integer userId = this.user01.getId();
        Role role = Role.USER;

        //Act
        String result = userRepository.findUserRoleByUserId(userId);

        //Assert
        assertNotNull(result);
        assertEquals(role.name(), result);
    }
}