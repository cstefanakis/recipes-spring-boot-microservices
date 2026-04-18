package com.recipe.recipestep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RecipeStepServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeStepServiceApplication.class, args);
	}

}
