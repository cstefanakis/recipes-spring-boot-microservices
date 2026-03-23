package com.example.recipe_service.services;

import com.example.recipe_service.dtos.category.CategoryCreateRequestDto;
import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.dtos.category.CategoryUpdateRequestDto;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setup(){
        this.category = Category.builder()
                .id(1)
                .name("Category")
                .imgUrl("url")
                .build();
    }

    @Test
    void existsCategoryById() {
        //Arrest
        Integer categoryId = this.category.getId();
        //Mock
        when(categoryRepository.existsById(categoryId))
                .thenReturn(true);
        //Act
        boolean result = categoryService.existsCategoryById(categoryId);
        //Assert
        assertTrue(result);
        //Verify
        verify(categoryRepository, times(1)).existsById(categoryId);
    }

    @Test
    void getCategoryById() {
        //Arrest
        Integer categoryId = this.category.getId();
        //Mock
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(this.category));
        //Act
        Category result = categoryService.getCategoryById(categoryId);
        //Assert
        assertNotNull(result);
        assertEquals(this.category.getId(), result.getId());
        assertEquals(this.category.getImgUrl(), result.getImgUrl());
        assertEquals(this.category.getName(), result.getName());
        //Verify
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void createCategory() {
        //Arrest
        CategoryCreateRequestDto categoryCreateRequestDto = CategoryCreateRequestDto.builder()
                .name(this.category.getName())
                .imgUrl(this.category.getImgUrl())
                .build();
        //Mock
        when(categoryRepository.save(any(Category.class))).thenReturn(this.category);
        //Act
        categoryService.createCategory(categoryCreateRequestDto);
        //Verify
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void toCategoryResponseDto() {
        //Act
        CategoryResponseDto result = categoryService.toCategoryResponseDto(this.category);
        //Assert
        assertNotNull(result);
        assertEquals(this.category.getName(), result.getName());
        assertEquals(this.category.getImgUrl(), result.getImgUrl());
        assertEquals(this.category.getId(), result.getId());
    }

    @Test
    void getCategories() {
        //Arrest
        List<Category> categories = List.of(this.category);
        //Mock
        when(categoryRepository.findAll()).thenReturn(categories);
        //Act
        List<CategoryResponseDto> result = categoryService.getCategories();
        //Assert
        assertNotNull(result);
        assertTrue(result.stream()
                .anyMatch(c -> c.getId().equals(this.category.getId())));
        assertTrue(result.stream()
                .anyMatch(c -> c.getName().equals(this.category.getName())));
        assertTrue(result.stream()
                .anyMatch(c -> c.getImgUrl().equals(this.category.getImgUrl())));
        //Verify
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryResponseDtoById() {
        //Arrest
        Integer categoryId = this.category.getId();
        //Mock
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.category));
        //Act
        CategoryResponseDto result = categoryService.getCategoryResponseDtoById(categoryId);
        //Assert
        assertNotNull(result);
        assertEquals(this.category.getId(), result.getId());
        assertEquals(this.category.getName(), result.getName());
        assertEquals(this.category.getImgUrl(), result.getImgUrl());
        //Verify
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void updateCategory() {
        //Arrest
        Integer categoryId = this.category.getId();
        Category updatedCategory = Category.builder()
                .name("new name")
                .imgUrl("new url")
                .build();

        CategoryUpdateRequestDto categoryUpdateRequestDto = CategoryUpdateRequestDto.builder()
                .name("new name")
                .imgUrl("new url")
                .build();
        //Mock
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        //Act
        categoryService.updateCategory(categoryId, categoryUpdateRequestDto);
        //Verify
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void deleteCategoryById() {
        //Arrest
        Integer categoryId = this.category.getId();
        //Mock
        doNothing().when(categoryRepository).deleteById(categoryId);
        //Act
        categoryService.deleteCategoryById(categoryId);
        //Verify
        verify(categoryRepository).deleteById(categoryId);
    }
}