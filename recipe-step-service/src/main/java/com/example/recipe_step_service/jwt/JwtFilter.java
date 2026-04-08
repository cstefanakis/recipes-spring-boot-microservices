package com.example.recipe_step_service.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // 1. Check header exists
        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            //Validate token FIRST
            if (jwtUtil.isTokenValid(token)) {

                //Extract data from token
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                //Convert role
                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + role));

                //Create authentication object
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                authorities
                        );

                //Set user in Spring Security context
                SecurityContextHolder.getContext().setAuthentication(auth);

            } else {
                //Invalid token
                SecurityContextHolder.clearContext();
            }
        }

        //Continue request
        filterChain.doFilter(request, response);
    }
}
