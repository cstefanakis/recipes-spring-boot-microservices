package com.example.Ingredients_service.services;

import com.example.Ingredients_service.dtos.IngredientDto;
import com.example.Ingredients_service.models.Ingredient;
import com.example.Ingredients_service.repositories.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    private IngredientDto tomato;
    private Ingredient savedTomato;

    @BeforeEach()
    void setup(){

        this.tomato = IngredientDto.builder()
                .name("Tomato")
                .categoriesId(List.of(1))
                .build();

        this.savedTomato = Ingredient.builder()
                .id(1)
                .name(this.tomato.getName())
                .categoriesId(this.tomato.getCategoriesId())
                .build();
    }

    @Test
    void createIngredient() {
        //Arrest
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(savedTomato);
        //Act
        ingredientService.createIngredient(this.tomato);
        //Assert
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    void getIngredientById() {
    }

    @Test
    void deleteIngredient() {
    }

    @Test
    void updateIngredient() {
    }

    @Test
    void getAllIngredients() {
    }
}