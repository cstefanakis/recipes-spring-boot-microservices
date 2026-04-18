package com.recipe.recipe.clients;

import com.recipe.recipe.dtos.user.UserResponseDto;
import com.recipe.recipe.dtos.user.UserResponseIdAndRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface UserClient {

    @GetMapping("/api/auth/profile-id-role")
    ResponseEntity<UserResponseIdAndRole> authenticatedUserIdAndRole();

    @GetMapping("/users/{userId}")
    ResponseEntity<UserResponseDto> getUserById(@PathVariable("userId") Integer userId);

}
