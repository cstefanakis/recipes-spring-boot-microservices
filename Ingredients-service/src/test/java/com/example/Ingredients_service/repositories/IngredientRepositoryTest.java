package com.example.Ingredients_service.repositories;

import com.example.Ingredients_service.models.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

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

    @Test
    void findAllIds() {
        //Act
        List<Integer> result = ingredientRepository.findAllIds();
        //Assert
        assertNotNull(result);
        assertTrue(result.contains(this.tomato.getId()));
    }

    @Test
    void findIngredientNameById() {
        //Arrest
        Integer tomatoId = this.tomato.getId();
        String name = this.tomato.getName();
        //Act
        Optional<String> result = ingredientRepository.findIngredientNameById(tomatoId);
        //Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(name, result.get());
    }

    @Test
    void findAllIdByCategoryId() {
        //Arrest
        Integer vegetablesId = 1;
        Integer tomatoId = this.tomato.getId();
        //Act
        List<Integer> result = ingredientRepository.findAllIdByCategoryId(vegetablesId);
        //Assert
        assertNotNull(result);
        assertTrue(result.contains(tomatoId));
    }
}