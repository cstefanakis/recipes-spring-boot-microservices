package com.example.Ingredients_service.services;

import com.example.Ingredients_service.dtos.ingredient.IngredientCreateRequestDto;
import com.example.Ingredients_service.dtos.ingredient.IngredientResponseDto;
import com.example.Ingredients_service.dtos.ingredient.IngredientSimpleResponseDto;
import com.example.Ingredients_service.dtos.ingredient.IngredientUpdateRequestDto;
import com.example.Ingredients_service.models.Category;
import com.example.Ingredients_service.models.Ingredient;
import com.example.Ingredients_service.repositories.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private CategoryService categoryService;

    private Ingredient savedTomato;
    private Category vegetables;

    @BeforeEach()
    void setup(){
        this.vegetables = Category.builder()
                .id(1)
                .name("Vegetables")
                .imgUrl("url")
                .build();

        IngredientCreateRequestDto tomatoDto = IngredientCreateRequestDto.builder()
                .name("Tomato")
                .imgUrl("url")
                .categoriesId(List.of(1))
                .build();

        this.savedTomato = Ingredient.builder()
                .id(1)
                .name(tomatoDto.getName())
                .imgUrl("url")
                .categories(List.of(this.vegetables))
                .build();
    }

    @Test
    void createIngredient() {
        //Arrest
        Integer vegetablesId = this.vegetables.getId();

        IngredientCreateRequestDto ingredientCreateRequestDto = IngredientCreateRequestDto.builder()
                .name("Tomato")
                .imgUrl("Url")
                .categoriesId(List.of(vegetablesId))
                .build();
        //Mock
        when(categoryService.getCategoryById(vegetablesId))
                .thenReturn(this.vegetables);
        when(ingredientRepository.nameExists(any(String.class))).thenReturn(Boolean.FALSE);
        when(ingredientRepository.save(any(Ingredient.class)))
                .thenReturn(savedTomato);

        ArgumentCaptor<Ingredient> captor = ArgumentCaptor.forClass(Ingredient.class);
        //Act
        ingredientService.createIngredient(ingredientCreateRequestDto);
        //Verify
        verify(categoryService, times(1)).getCategoryById(vegetablesId);
        verify(ingredientRepository, times(1)).save(captor.capture());
        //Assert
        Ingredient result = captor.getValue();
        assertEquals(ingredientCreateRequestDto.getName(), result.getName());
        assertEquals(ingredientCreateRequestDto.getImgUrl(), result.getImgUrl());
    }

    @Test
    void getIngredientWithCategoryById() {
        //Arrest
        Integer tomatoId = this.savedTomato.getId();

        when(ingredientRepository.findById(tomatoId)).thenReturn(Optional.of(savedTomato));
        //Act
        IngredientResponseDto result = ingredientService.getIngredientWithCategoryById(tomatoId);
        //Assert
        assertNotNull(result);
        assertEquals(this.savedTomato.getName(), result.getName());
        verify(ingredientRepository, times(1)).findById(tomatoId);
    }

    @Test
    void deleteIngredient() {
        //Arrest
        Integer tomatoId = this.savedTomato.getId();
        //Act
        ingredientService.deleteIngredient(tomatoId);
        //Assert
        verify(ingredientRepository, times(1)).deleteById(tomatoId);
    }

    @Test
    void updateIngredient() {
        //Arrest
        Integer tomatoId = this.savedTomato.getId();
        IngredientUpdateRequestDto tomatoUpdateDto = IngredientUpdateRequestDto.builder()
                .name("chocolate")
                .categoriesId(List.of(1))
                .imgUrl("new Url")
                .build();

        Ingredient updatedIngredient = Ingredient.builder()
                .id(this.savedTomato.getId())
                .name(tomatoUpdateDto.getName())
                .imgUrl(tomatoUpdateDto.getImgUrl())
                .categories(List.of(this.vegetables))
                .build();
        //Mock
        when(ingredientRepository.findById(tomatoId)).thenReturn(Optional.of(this.savedTomato));

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(updatedIngredient);
        //Act
        Ingredient result = ingredientService.updateIngredient(tomatoId, tomatoUpdateDto);
        //Assert
        assertNotNull(result);
        assertEquals(tomatoUpdateDto.getName(), result.getName());
        assertEquals(tomatoUpdateDto.getImgUrl(), result.getImgUrl());
        assertTrue(result.getCategories().stream().anyMatch(i -> i.getId() == 1));
        //Verify
        verify(ingredientRepository, times(1)).findById(tomatoId);
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    void getIngredientSimpleResponseDtoById() {
        //Arrest
        Integer ingredientId = this.savedTomato.getId();
        //Mock
        when(ingredientRepository.findById(ingredientId))
                .thenReturn(Optional.of(this.savedTomato));
        //Act
        IngredientSimpleResponseDto result = ingredientService.getIngredientSimpleResponseDtoById(ingredientId);
        //Assert
        assertNotNull(result);
        assertEquals(this.savedTomato.getName(), result.getName());
        assertEquals(this.savedTomato.getImgUrl(), result.getImgUrl());
        assertEquals(this.savedTomato.getId(), result.getId());
        //Verify
        verify(ingredientRepository, times(1)).findById(ingredientId);
    }

    @Test
    void getAllSimpleIngredients() {
        //Arrest
        Page<Ingredient> ingredients = new PageImpl<>(List.of(this.savedTomato));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        //Mock
        when(ingredientRepository.findAll(pageable)).thenReturn(ingredients);
        //Act
        Page<IngredientSimpleResponseDto> result = ingredientService.getAllSimpleIngredients(pageable);
        //Assert
        assertNotNull(result);
        assertEquals(1, result.getSize());
        assertTrue(result.stream().anyMatch(i -> i.getName().equals(this.savedTomato.getName())));
        //Verify
        verify(ingredientRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllSimpleIngredientsByCategoryId() {
        //Arrest
        Integer categoryId = this.vegetables.getId();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Ingredient> ingredients = new PageImpl<>(List.of(this.savedTomato));
        //Mock
        when(ingredientRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(ingredients);
        //Act
        Page<IngredientSimpleResponseDto> result = ingredientService.getAllSimpleIngredientsByCategoryId(categoryId, pageable);
        //Assert
        assertNotNull(result);
        assertEquals(1, result.getSize());
        assertTrue(result.stream().anyMatch(i -> i.getName().equals(this.savedTomato.getName())));
        assertTrue(result.stream().anyMatch(i -> i.getImgUrl().equals(this.savedTomato.getImgUrl())));
        assertTrue(result.stream().anyMatch(i -> i.getId().equals(this.savedTomato.getId())));
        //Verify
        verify(ingredientRepository, times(1)).findAllByCategoryId(
                categoryId, pageable);
    }

    @Test
    void getIngredientsSimpleResponseDtoByName() {
        //Arrest
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        String name = "tom";
        Page<Ingredient> ingredients = new PageImpl<>(List.of(this.savedTomato));
        //Mock
        when(ingredientRepository.findAllByName(name, pageable)).thenReturn(ingredients);
        //Act
        Page<IngredientSimpleResponseDto> result = ingredientService.getIngredientsSimpleResponseDtoByName(name, pageable);
        //Assert
        assertNotNull(result);
        assertEquals(1, result.getSize());
        assertTrue(result.stream().anyMatch(i -> i.getName().equals(this.savedTomato.getName())));
        assertTrue(result.stream().anyMatch(i -> i.getImgUrl().equals(this.savedTomato.getImgUrl())));
        assertTrue(result.stream().anyMatch(i -> i.getId().equals(this.savedTomato.getId())));
        //Verify
        verify(ingredientRepository).findAllByName(name,pageable);
    }
}