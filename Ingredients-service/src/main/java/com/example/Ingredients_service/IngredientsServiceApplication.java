package com.example.Ingredients_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IngredientsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IngredientsServiceApplication.class, args);
	}

}
