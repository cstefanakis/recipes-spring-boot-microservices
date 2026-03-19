package com.example.Ingredients_service.repositories;

import com.example.Ingredients_service.models.Category;
import com.example.Ingredients_service.models.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Ingredient tomato;

    @BeforeEach()
    void setup(){
        Category vegetables = categoryRepository.save(Category.builder()
                        .name("Vegetables")
                        .imgUrl("url")
                .build());

        this.tomato = ingredientRepository.save(Ingredient.builder()
                        .name("Tomato")
                        .categories(List.of(vegetables))
                .build());
    }
}