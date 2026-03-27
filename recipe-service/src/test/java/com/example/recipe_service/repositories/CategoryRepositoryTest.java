package com.example.recipe_service.repositories;

import com.example.recipe_service.models.Category;
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
                        .name("Sweets")
                        .imgUrl("http://img.png")
                .build());
    }

    @Test
    void findByName_True() {
        //Arrange
        String categoryName = "sweets";
        //Act
        boolean result = categoryRepository.existsByName(categoryName);
        //Assert
        assertTrue(result);
    }

    @Test
    void findByName_False() {
        //Arrange
        String categoryName = "sweet";
        //Act
        boolean result = categoryRepository.existsByName(categoryName);
        //Assert
        assertFalse(result);
    }
}