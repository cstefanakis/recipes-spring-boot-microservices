package com.example.Ingredients_service.repositories;

import com.example.Ingredients_service.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup(){
        categoryRepository.save(Category.builder()
                        .name("Vegetables")
                        .imgUrl("url")
                .build());
    }

    @Test
    void nameExists_true() {
        String name = "Vegetables";
        //Act
        boolean result = categoryRepository.nameExists(name);
        //Assert
        assertTrue(result);
    }

    @Test
    void nameExists_trueWithLow() {
        String name = "vegetables";
        //Act
        boolean result = categoryRepository.nameExists(name);
        //Assert
        assertTrue(result);
    }

    @Test
    void nameExists_false() {
        String name = "Meals";
        //Act
        boolean result = categoryRepository.nameExists(name);
        //Assert
        assertFalse(result);
    }
}