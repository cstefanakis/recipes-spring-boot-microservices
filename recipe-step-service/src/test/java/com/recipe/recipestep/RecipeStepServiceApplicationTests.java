package com.recipe.recipestep;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "security.jwt.secret-key=test-secret-key"})
class RecipeStepServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
