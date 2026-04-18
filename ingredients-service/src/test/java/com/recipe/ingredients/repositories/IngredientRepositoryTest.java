package com.recipe.ingredients.repositories;

import com.recipe.ingredients.models.Category;
import com.recipe.ingredients.models.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                        .imgUrl("url")
                        .categories(List.of(vegetables))
                .build());
    }

    @Test
    void findAllByCategoryId() {
        //Arrange
        Integer categoryId = this.tomato.getId();
        Pageable pageable = PageRequest.of(0, 10);
        //Act
        Page<Ingredient> result = ingredientRepository.findAllByCategoryId(categoryId, pageable);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(i -> i.getName().equals(this.tomato.getName())));
        assertTrue(result.stream().anyMatch(i -> i.getImgUrl().equals(this.tomato.getImgUrl())));
        assertTrue(result.stream().anyMatch(i -> i.getId().equals(this.tomato.getId())));
    }

    @Test
    void findAllByName() {
        //Arrange
        String name = this.tomato.getName();
        Pageable pageable = PageRequest.of(0, 10);
        //Act
        Page<Ingredient> result = ingredientRepository.findAllByName(name, pageable);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(i -> i.getName().equals(this.tomato.getName())));
        assertTrue(result.stream().anyMatch(i -> i.getImgUrl().equals(this.tomato.getImgUrl())));
        assertTrue(result.stream().anyMatch(i -> i.getId().equals(this.tomato.getId())));
    }

    @Test
    void nameExists_true() {
        //Arrange
        String name = "Tomato";
        //Act
        boolean result = ingredientRepository.nameExists(name);
        //Assert
        assertTrue(result);
    }

    @Test
    void nameExists_trueWithLow() {
        //Arrange
        String name = "Tomato";
        //Act
        boolean result = ingredientRepository.nameExists(name);
        //Assert
        assertTrue(result);
    }

    @Test
    void nameExists_false() {
        //Arrange
        String name = "beans";
        //Act
        boolean result = ingredientRepository.nameExists(name);
        //Assert
        assertFalse(result);
    }

    @Test
    void ingredientId() {
        //Arrange
        Integer ingredientId = this.tomato.getId();
        //Act
        Integer result = ingredientRepository.ingredientId(ingredientId);
        //Assert
        assertEquals(ingredientId, result);
    }
}