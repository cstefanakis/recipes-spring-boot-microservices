package com.example.Ingredients_service.repositories;

import com.example.Ingredients_service.models.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private Ingredient tomato;

    @BeforeEach()
    void setup(){
        this.tomato = ingredientRepository.save(Ingredient.builder()
                        .name("Tomato")
                        .categoriesId(List.of(1))
                .build());
    }

    @Test
    void findAllByCategoryId() {
        //Arrest
        Integer vegetablesId = 1;
        //Act
        List<Ingredient> result = ingredientRepository.findAllByCategoryId(vegetablesId);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(i -> i.getName().equals(this.tomato.getName())));
    }
}