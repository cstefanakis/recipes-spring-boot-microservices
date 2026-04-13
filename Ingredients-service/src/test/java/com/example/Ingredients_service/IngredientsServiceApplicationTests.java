package com.example.Ingredients_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "security.jwt.secret-key=test-secret-key"})
class IngredientsServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
