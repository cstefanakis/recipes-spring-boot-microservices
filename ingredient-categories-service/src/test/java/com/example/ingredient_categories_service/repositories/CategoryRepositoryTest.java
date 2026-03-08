package com.example.ingredient_categories_service.repositories;

import com.example.ingredient_categories_service.models.Category;
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

    private Category vegetables;

    @BeforeEach
    void setup(){
        this.vegetables = categoryRepository.save(
                Category.builder()
                        .name("Vegetables")
                        .build()
        );
    }

    @Test
    void findCategoryNameByCategoryId() {
        //Arrest
        Integer categoryId = this.vegetables.getId();
        String categoryName = this.vegetables.getName();
        //Act
        String result = categoryRepository.findCategoryNameByCategoryId(categoryId);
        //Assert
        assertNotNull(result);
        assertTrue(result.contains(categoryName));
    }
}