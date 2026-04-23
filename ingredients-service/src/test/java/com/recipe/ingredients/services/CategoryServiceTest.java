package com.recipe.ingredients.services;

import com.recipe.ingredients.dtos.category.CategoryCreateRequestDto;
import com.recipe.ingredients.dtos.category.CategoryResponseDto;
import com.recipe.ingredients.dtos.category.CategoryUpdateRequestDto;
import com.recipe.ingredients.models.Category;
import com.recipe.ingredients.repositories.CategoryRepository;
import jakarta.persistence.EntityExistsException;
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
    private CategoryUpdateRequestDto categoryUpdateRequestDto;
    private CategoryCreateRequestDto categoryCreateRequestDto;

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

        this.categoryUpdateRequestDto = CategoryUpdateRequestDto.builder()
                .name("new name")
                .imgUrl("new url")
                .build();

        this.categoryCreateRequestDto = CategoryCreateRequestDto.builder()
                .name(this.vegetables.getName())
                .imgUrl(this.vegetables.getImgUrl())
                .build();
    }

    @Test
    void createCategory() {
        //Mock
        when(categoryRepository.nameExists(any(String.class))).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(this.savedVegetables);
        //Act
        categoryService.createCategory(categoryCreateRequestDto);
        //Verify
        verify(categoryRepository).nameExists(any(String.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_NameExists() {
        //Mock
        when(categoryRepository.nameExists(any(String.class))).thenReturn(true);
        //Act
        assertThrows(EntityExistsException.class,
                () ->
            categoryService.createCategory(categoryCreateRequestDto));
        //Verify
        verify(categoryRepository).nameExists(any(String.class));
    }

    @Test
    void updateCategory() {
        //Arrest
        Integer categoryId = this.savedVegetables.getId();

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
    void updateCategory_equalsName() {
        //Arrest
        Integer categoryId = this.savedVegetables.getId();

        this.savedVegetables.setName(categoryUpdateRequestDto.getName());
        this.savedVegetables.setImgUrl(categoryUpdateRequestDto.getImgUrl());

        //Mock
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(this.savedVegetables));

        //Act
        categoryService.updateCategory(categoryId, this.categoryUpdateRequestDto);

        //Verify
        verify(categoryRepository, times(1))
                .findById(categoryId);
    }

    @Test
    void updateCategory_NameExists() {
        //Arrest
        Integer categoryId = this.savedVegetables.getId();

        this.vegetables.setName("existsName");
        this.vegetables.setImgUrl(categoryUpdateRequestDto.getImgUrl());

        //Mock
        when(categoryRepository.nameExists(any(String.class))).thenReturn(true);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.vegetables));
        //Act
        assertThrows(EntityExistsException.class,
                () -> categoryService.updateCategory(categoryId, categoryUpdateRequestDto));
        //Verify
        verify(categoryRepository).nameExists(any(String.class));
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

    @Test
    void getCategoryResponseDtoById() {
        //Arrest
        Integer categoryId = 1;
        //Mock
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.vegetables));
        //Act
        CategoryResponseDto result = categoryService.getCategoryResponseDtoById(categoryId);
        //Assert
        assertNotNull(result);
        assertEquals(this.vegetables.getName(), result.getName());
        assertEquals(this.vegetables.getImgUrl(), result.getImgUrl());
        assertEquals(this.vegetables.getId(), result.getId());
        //Verify
        verify(categoryRepository).findById(categoryId);
    }
}