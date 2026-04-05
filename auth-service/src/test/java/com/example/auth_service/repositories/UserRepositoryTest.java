package com.example.auth_service.repositories;

import com.example.auth_service.models.User;
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
                .password("pass")
                .build());
    }

    @Test
    void findByUsername() {
        //Arrange
        String username = this.user01.getUsername();

        //Act
        Optional<User> result = userRepository.findByUsername(username);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
    }
}