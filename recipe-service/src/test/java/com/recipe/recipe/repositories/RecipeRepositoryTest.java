package com.recipe.recipe.repositories;

import com.recipe.recipe.models.Category;
import com.recipe.recipe.models.Recipe;
import com.recipe.recipe.models.RecipeIngredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.recipe.recipe.enums.Unit.GRAM;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    private Recipe recipe;
    private RecipeIngredient recipeIngredient;
    private Category category;

    @BeforeEach
    void setup(){

        this.category = categoryRepository.save(Category.builder()
                .name("Category")
                .imgUrl("url")
                .build());

        this.recipe = recipeRepository.save(Recipe.builder()
                .title("title")
                .description("description")
                .imgUrl("url")
                .categories(List.of(category))
                .userId(1)
                .build());

        this.recipeIngredient = recipeIngredientRepository.save(RecipeIngredient.builder()
                .ingredientId(1)
                .unit(GRAM)
                .quantity(500.0)
                .recipe(this.recipe)
                .build());
    }

    @Test
    void findRecipeByCategoryId() {
        //Arrest
        Integer categoryId = this.category.getId();
        Pageable pageable = PageRequest.of(0, 10);

        //Act
        Page<Recipe> result = recipeRepository.findRecipesByCategoryId(categoryId, pageable);

        //Assert
        assertNotNull(result);
        assertTrue(result.stream()
                .anyMatch(r -> r.getTitle().equals(this.recipe.getTitle())));
        assertTrue(result.stream()
                .anyMatch(r -> r.getDescription().equals(this.recipe.getDescription())));
        assertTrue(result.stream()
                .anyMatch(r -> r.getImgUrl().equals(this.recipe.getImgUrl())));
        assertTrue(result.stream()
                .anyMatch(r -> r.getId().equals(this.recipe.getId())));
    }

    @Test
    void titleExists() {
        //Arrange
        String title = "Title";
        //Act
        boolean result = recipeRepository.titleExists(title);
        //Assert
        assertTrue(result);
    }

    @Test
    void findRecipeOwnerIdByRecipeId() {
        //Arrange
        Integer recipeId = this.recipe.getId();
        Integer userId = this.recipe.getUserId();
        //Act
        Integer result = recipeRepository.findRecipeOwnerIdByRecipeId(recipeId);
        //Assert
        assertNotNull(result);
        assertEquals(userId, result);
    }

    @Test
    void findAllAuthorSimpleRecipes() {
        //Arrange
        Integer userId = this.recipe.getUserId();
        Pageable pageable = PageRequest.of(0, 10);

        //Act
        Page<Recipe> result = recipeRepository.findAllAuthorSimpleRecipes(userId, pageable);

        //Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(this.recipe.getId(), result.getContent().getFirst().getId());
        assertEquals(this.recipe.getTitle(), result.getContent().getFirst().getTitle());
    }
}