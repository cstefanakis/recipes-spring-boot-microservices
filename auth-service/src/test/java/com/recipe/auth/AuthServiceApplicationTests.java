package com.recipe.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
        "security.jwt.secret-key=test-secret-key"})
@ActiveProfiles("test")
class AuthServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
