package com.example.Ingredients_service.services;

import com.example.Ingredients_service.dtos.category.CategoryCreateRequestDto;
import com.example.Ingredients_service.dtos.category.CategoryResponseDto;
import com.example.Ingredients_service.dtos.category.CategoryUpdateRequestDto;
import com.example.Ingredients_service.models.Category;
import com.example.Ingredients_service.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category vegetables;
    private Category savedVegetables;
    private Category savedMeals;

    @BeforeEach
    void setup(){
        this.vegetables = Category.builder()
                .name("Vegetables")
                .imgUrl("url")
                .build();

        this.savedVegetables = Category.builder()
                .id(1)
                .name("Vegetables")
                .imgUrl("url")
                .build();

        this.savedMeals = Category.builder()
                .id(2)
                .name("Meals")
                .imgUrl("url")
                .build();
    }

    @Test
    void createCategory() {
        //Arrest
        CategoryCreateRequestDto categoryCreateRequestDto = CategoryCreateRequestDto.builder()
                .name(this.vegetables.getName())
                .imgUrl(this.vegetables.getImgUrl())
                .build();

        //Mock
        when(categoryRepository.save(any(Category.class))).thenReturn(this.savedVegetables);
        //Act
        categoryService.createCategory(categoryCreateRequestDto);
        //Verify
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory() {
        //Arrest
        Integer categoryId = this.savedVegetables.getId();
        CategoryUpdateRequestDto categoryUpdateRequestDto = CategoryUpdateRequestDto.builder()
                .name("new name")
                .imgUrl("new url")
                .build();
        this.vegetables.setName(categoryUpdateRequestDto.getName());
        this.vegetables.setImgUrl(categoryUpdateRequestDto.getImgUrl());

        //Mock
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.vegetables));
        //Act
        categoryService.updateCategory(categoryId, categoryUpdateRequestDto);
        //Verify
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void getAllCategoryResponseDto() {
        //Arrest
        List<Category> categories = List.of(this.savedVegetables, this.savedMeals);
        //Mock
        when(categoryRepository.findAll()).thenReturn(categories);
        //Act
        List<CategoryResponseDto> result = categoryService.getAllCategoryResponseDto();
        //Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c-> c.getName().equals(this.savedVegetables.getName())));
        assertTrue(result.stream().anyMatch(c-> c.getName().equals(this.savedMeals.getName())));
        //Verify
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById() {
        //Arrest
        Integer categoryId = this.savedVegetables.getId();
        //Mock
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.savedVegetables));
        //Act
        Category category = categoryService.getCategoryById(categoryId);
        //Assert
        assertNotNull(category);
        assertEquals(this.savedVegetables.getName(), category.getName());
        assertEquals(this.savedVegetables.getImgUrl(), category.getImgUrl());
        //Verify
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void getCategoryById_NotFound() {
        //Arrest
        Integer categoryId = this.savedVegetables.getId();
        //Mock
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        //Act and Assert
        RuntimeException exception =  assertThrows(EntityNotFoundException.class, () ->
                categoryService.getCategoryById(categoryId));

        assertEquals("Category with id: 1 not found", exception.getMessage());
        //Verify
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void toCategoriesResponseDto() {
        //Arrest
        List<Category> categories = List.of(this.savedVegetables, this.savedMeals);
        //Act
        List<CategoryResponseDto> result = categoryService.toCategoriesResponseDto(categories);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(cDto -> cDto.getName().equals(this.savedVegetables.getName())));
        assertTrue(result.stream().anyMatch(cDto -> cDto.getImgUrl().equals(this.savedVegetables.getImgUrl())));
    }

    @Test
    void deleteCategoryById() {
        //Arrest
        Integer categoryId = this.savedVegetables.getId();
        //Act
        categoryService.deleteCategoryById(categoryId);
        //Verify
        verify(categoryRepository, times(1))
                .deleteById(categoryId);
    }
}