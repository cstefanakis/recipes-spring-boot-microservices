package com.example.recipe_service.repositories;

import com.example.recipe_service.models.Category;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.models.RecipeIngredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.recipe_service.enums.Unit.GRAM;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RecipeIngredientRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    private Recipe recipe;
    private RecipeIngredient recipeIngredient;

    @BeforeEach
    void setup(){
        Category category = categoryRepository.save(Category.builder()
                .name("Category")
                .imgUrl("url")
                .build());

        this.recipe = recipeRepository.save(Recipe.builder()
                .title("title")
                .description("description")
                .imgUrl("url")
                .categories(List.of(category))
                .build());

        this.recipeIngredient = recipeIngredientRepository.save(RecipeIngredient.builder()
                        .ingredientId(1)
                        .unit(GRAM)
                        .quantity(500.0)
                        .recipe(this.recipe)
                .build());
    }

    @Test
    void findRecipeIngredientsByRecipeId() {
        //Arrest
        Integer recipeId = this.recipe.getId();
        //Act
        List<RecipeIngredient> result = recipeIngredientRepository.findRecipeIngredientsByRecipeId(recipeId);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream()
                .anyMatch(ri -> this.recipeIngredient.getId().equals(ri.getId())));
        assertTrue(result.stream()
                .anyMatch(ri -> this.recipeIngredient.getIngredientId().equals(ri.getIngredientId())));
        assertTrue(result.stream()
                .anyMatch(ri -> this.recipeIngredient.getUnit().equals(ri.getUnit())));
        assertTrue(result.stream()
                .anyMatch(ri -> this.recipeIngredient.getQuantity().equals(ri.getQuantity())));
    }

    @Test
    void existsWithIngredientId() {
        //Arrange
        Integer ingredientId = this.recipeIngredient.getIngredientId();
        //Act
        boolean result = recipeIngredientRepository.existsWithIngredientId(ingredientId);
        //Assert
        assertTrue(result);
    }
}