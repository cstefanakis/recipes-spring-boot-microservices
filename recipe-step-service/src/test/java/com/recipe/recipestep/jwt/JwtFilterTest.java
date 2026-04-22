package com.recipe.recipestep.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSetAuthentication_tokenValid() throws Exception {
        // Arrange
        String token = "valid-token";

        //Mock
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        when(jwtUtil.isTokenValid(token))
                .thenReturn(true);
        when(jwtUtil.extractUsername(token))
                .thenReturn("user1");
        when(jwtUtil.extractRole(token))
                .thenReturn("USER");

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Assert
        assertNotNull(auth);
        assertEquals("user1", auth.getPrincipal());
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("USER")));

        //Verify
        verify(filterChain)
                .doFilter(request, response);
    }

    @Test
    void shouldClearContext_InvalidToken() throws Exception {
        // Arrange
        String token = "invalid-token";

        //Mock
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        when(jwtUtil.isTokenValid(token))
                .thenReturn(false);

        //Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //Assert
        assertNull(auth);

        //Verify
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldSkip_whenNoAuthorizationHeader() throws Exception {
        //Mock
        when(request.getHeader("Authorization"))
                .thenReturn(null);

        //Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //Assert
        assertNull(auth);

        //Verify
        verify(filterChain).doFilter(request, response);
    }
}