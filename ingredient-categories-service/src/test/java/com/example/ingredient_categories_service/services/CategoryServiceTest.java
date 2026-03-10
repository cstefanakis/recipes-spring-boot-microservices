package com.example.ingredient_categories_service.services;

import com.example.ingredient_categories_service.clients.IngredientClient;
import com.example.ingredient_categories_service.dtos.CategoryDto;
import com.example.ingredient_categories_service.dtos.FullResponseCategoryDto;
import com.example.ingredient_categories_service.dtos.IngredientDto;
import com.example.ingredient_categories_service.dtos.UpdateCategoryDto;
import com.example.ingredient_categories_service.models.Category;
import com.example.ingredient_categories_service.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private IngredientClient ingredientClient;

    private Category vegetables;
    private Category savedVegetables;

    @BeforeEach
    void setup(){
        this.vegetables = Category.builder()
                .name("Vegetables")
                .build();
        this.savedVegetables = Category.builder()
                .id(1)
                .name("Vegetables")
                .build();
    }

    @Test
    void createCategory() {
        //Arrest
        CategoryDto categoryDto = CategoryDto.builder()
                .name("Vegetables")
                .build();
        when(categoryRepository.save(any(Category.class))).thenReturn(this.savedVegetables);
        //Act
        Category result = categoryService.createCategory(categoryDto);
        //Assert
        assertNotNull(result);
        assertEquals(this.savedVegetables, result);
        verify(categoryRepository ,times(1)).save(any(Category.class));
    }

    @Test
    void deleteCategory() {
        //Arrest
        Integer categoryId = this.savedVegetables.getId();
        //Act
        categoryService.deleteCategory(categoryId);
        //Assert
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void updateCategory() {
        //Arrest
        Integer categoryId = this.savedVegetables.getId();

        UpdateCategoryDto updateDto = UpdateCategoryDto.builder()
                .name("meals")
                .build();

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(this.savedVegetables));

        this.vegetables.setName(updateDto.getName());

        when(categoryRepository.save(any(Category.class))).thenReturn(this.vegetables);

        //Act
        Category result = categoryService.updateCategory(categoryId, updateDto);
        //Assert
        assertNotNull(result);
        assertEquals(updateDto.getName(), result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void getAllCategories() {
        //Arrest
        List<Category> categories = List.of(this.savedVegetables);
        when(categoryRepository.findAll()).thenReturn(categories);
        //Act
        List<Category> result = categoryService.getAllCategories();
        //Assert
        assertNotNull(result);
        assertTrue(result.contains(this.savedVegetables));
        verify(categoryRepository, times(1)).findAll();

    }

    @Test
    void getCategoryWithIngredientsById() {
        //Arrest
        IngredientDto tomato = IngredientDto.builder()
                .name("Tomato")
                .build();

        Integer categoryId = this.savedVegetables.getId();

        String categoryName = this.savedVegetables.getName();

        List<IngredientDto> ingredients = List.of(tomato);

        when(categoryRepository.findCategoryNameByCategoryId(categoryId)).thenReturn(categoryName);

        when(ingredientClient.getSimpleIngredientByCategoryId(categoryId)).thenReturn(ingredients);

        //Act
        FullResponseCategoryDto result = categoryService.getCategoryWithIngredientsById(categoryId);

        //Assert
        assertNotNull(result);
        assertEquals(categoryName, result.getName());
        assertTrue(result.getIngredients().stream()
                .anyMatch(ingredient -> ingredient.getName().equals(tomato.getName())));
        verify(categoryRepository, times(1)).findCategoryNameByCategoryId(categoryId);
        verify(ingredientClient, times(1)).getSimpleIngredientByCategoryId(categoryId);
    }

    @Test
    void getCategoryById() {
        //Arrest
        Integer categoryId = this.savedVegetables.getId();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.savedVegetables));
        //Act
        Category result = categoryService.getCategoryById(categoryId);
        //Assert
        assertNotNull(result);
        assertEquals(this.savedVegetables, result);
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void categoryExists_isTrue() {
        //Arrest
        Integer categoryId = 1;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        //Act
        boolean result = categoryService.categoryExists(categoryId);
        //Assert
        assertTrue(result);
        //Verify
        verify(categoryRepository).existsById(categoryId);
    }

    @Test
    void categoryExists_isFalse() {
        //Arrest
        Integer categoryId = 1;
        when(categoryRepository.existsById(categoryId)).thenReturn(false);
        //Act
        boolean result = categoryService.categoryExists(categoryId);
        //Assert
        assertFalse(result);
        //Verify
        verify(categoryRepository).existsById(categoryId);
    }
}