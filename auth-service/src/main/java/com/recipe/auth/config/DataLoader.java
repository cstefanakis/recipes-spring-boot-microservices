package com.recipe.auth.config;

import com.recipe.auth.models.Role;
import com.recipe.auth.models.User;
import com.recipe.auth.repositories.UserRepository;
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
