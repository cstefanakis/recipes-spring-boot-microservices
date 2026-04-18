package com.recipe.recipe.services;

import com.recipe.recipe.dtos.category.CategoryCreateRequestDto;
import com.recipe.recipe.dtos.category.CategoryResponseDto;
import com.recipe.recipe.dtos.category.CategoryUpdateRequestDto;
import com.recipe.recipe.models.Category;
import com.recipe.recipe.repositories.CategoryRepository;
import jakarta.persistence.EntityExistsException;
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
        when(categoryRepository.existsByName(any(String.class))).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(this.category);
        //Act
        categoryService.createCategory(categoryCreateRequestDto);
        //Verify
        verify(categoryRepository, times(1)).existsByName(any(String.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_NameExists() {
        //Arrest
        CategoryCreateRequestDto categoryCreateRequestDto = CategoryCreateRequestDto.builder()
                .name(this.category.getName())
                .imgUrl(this.category.getImgUrl())
                .build();
        //Mock
        when(categoryRepository.existsByName(any(String.class))).thenReturn(true);
        //Act
        assertThrows(EntityExistsException.class,
                ()-> categoryService.createCategory(categoryCreateRequestDto));
        //Verify
        verify(categoryRepository).existsByName(any(String.class));
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
                .imgUrl("http://newImg.png")
                .build();

        CategoryUpdateRequestDto categoryUpdateRequestDto = CategoryUpdateRequestDto.builder()
                .name("new name")
                .imgUrl("http://newImg.png")
                .build();
        //Mock
        when(categoryRepository.existsByName(any(String.class))).thenReturn(false);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        //Act
        categoryService.updateCategory(categoryId, categoryUpdateRequestDto);
        //Verify
        verify(categoryRepository, times(1)).existsByName(any(String.class));
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_NameExists() {
        //Arrest
        Integer categoryId = this.category.getId();

        CategoryUpdateRequestDto categoryUpdateRequestDto = CategoryUpdateRequestDto.builder()
                .name("new name")
                .imgUrl("http://newImg.png")
                .build();
        //Mock
        when(categoryRepository.existsByName(any(String.class))).thenReturn(true);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.category));
        //Act
        assertThrows(EntityExistsException.class,
                ()-> categoryService.updateCategory(categoryId, categoryUpdateRequestDto));
        //Verify
        verify(categoryRepository, times(1)).existsByName(any(String.class));
        verify(categoryRepository, times(1)).findById(categoryId);
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