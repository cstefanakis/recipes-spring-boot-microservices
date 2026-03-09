package com.example.ingredient_categories_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IngredientCategoriesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IngredientCategoriesServiceApplication.class, args);
	}

}
