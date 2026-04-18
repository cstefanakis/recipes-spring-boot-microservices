package com.recipe.ingredients.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    private final String SECRET = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNzc2MDcwMDU2LCJleHAiOjE3NzYwNzM2NTZ9.ZXlKaGJHY2lPaUpJVXpJMU5pSXNJblI1Y0NJNklrcFh";

    private String generateToken;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();

        Field field = JwtUtil.class.getDeclaredField("secret");
        field.setAccessible(true);
        field.set(jwtUtil, Base64.getEncoder().encodeToString(SECRET.getBytes()));


         this.generateToken = Jwts.builder()
                .setSubject("user1")
                .claim("role", "USER")
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    @Test
    void extractUsername() {
        //Arrange
        String token = this.generateToken;

        //Act
        String result = jwtUtil.extractUsername(token);

        //Assert
        assertNotNull(result);
        assertEquals("user1", result);
    }

    @Test
    void extractRole() {
        //Arrange
        String token = this.generateToken;

        //Act
        String result = jwtUtil.extractRole(token);

        //Assert
        assertNotNull(result);
        assertEquals("USER", result);
    }

    @Test
    void isTokenValid() {
        //Arrange
        String token = this.generateToken;

        //Act
        boolean result= jwtUtil.isTokenValid(token);

        //Assert
        assertTrue(result);
    }

    @Test
    void isTokenValid_false() {
        //Arrange
        String token = "";

        //Act
        boolean result= jwtUtil.isTokenValid(token);

        //Assert
        assertFalse(result);
    }
}