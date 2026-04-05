package com.example.auth_service.services;

import com.example.auth_service.dtos.UserRequestDto;
import com.example.auth_service.models.User;
import com.example.auth_service.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User createUser(UserRequestDto userRequestDto){
        User user = toEntity(userRequestDto);
        return userRepository.save(user);
    }

    private User toEntity(UserRequestDto userRequestDto) {
        return User.builder()
                .username(userRequestDto.getUsername())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .build();
    }

    public User getByUserName(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Username with name %s not found", username)));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByUserName(username);
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
