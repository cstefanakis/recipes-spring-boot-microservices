package com.example.auth_service.config;

import com.example.auth_service.models.Role;
import com.example.auth_service.models.User;
import com.example.auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.findByEmailOrUsername("admin").isPresent()) {
            User admin = userRepository.save(User.builder()
                    .email("admin@admin.com")
                    .username("admin")
                    .fullName("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .build());
        }
    }
}
