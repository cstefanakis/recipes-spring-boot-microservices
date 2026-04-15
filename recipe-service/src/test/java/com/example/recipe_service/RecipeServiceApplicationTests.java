package com.example.recipe_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "security.jwt.secret-key=test-secret-key"})
class RecipeServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
