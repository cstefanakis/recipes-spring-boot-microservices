package com.example.Ingredients_service.services;

import com.example.Ingredients_service.clients.IngredientCategoryClient;
import com.example.Ingredients_service.dtos.IngredientCategoryDto;
import com.example.Ingredients_service.dtos.IngredientDto;
import com.example.Ingredients_service.dtos.IngredientResponseDto;
import com.example.Ingredients_service.dtos.IngredientUpdateDto;
import com.example.Ingredients_service.models.Ingredient;
import com.example.Ingredients_service.repositories.IngredientRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientCategoryClient ingredientCategoryClient;

    private IngredientCategoryDto vegetables;
    private IngredientDto tomatoDto;
    private Ingredient savedTomato;

    @BeforeEach()
    void setup(){
        this.vegetables = IngredientCategoryDto.builder()
                .id(1)
                .name("Vegetables")
                .build();

        this.tomatoDto = IngredientDto.builder()
                .name("Tomato")
                .categoriesId(List.of(1))
                .build();

        this.savedTomato = Ingredient.builder()
                .id(1)
                .name(this.tomatoDto.getName())
                .categoriesId(this.tomatoDto.getCategoriesId())
                .build();
    }

    @Test
    void createIngredient() {
        //Arrest
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(savedTomato);
        //Act
        ingredientService.createIngredient(this.tomatoDto);
        //Assert
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    void getIngredientWithCategoryById() {
        //Arrest
        Integer tomatoId = this.savedTomato.getId();

        when(ingredientRepository.findById(tomatoId)).thenReturn(Optional.of(savedTomato));

        when(ingredientCategoryClient.getIngredientCategoryById(this.vegetables.getId())).thenReturn(this.vegetables);
        //Act
        IngredientResponseDto result = ingredientService.getIngredientWithCategoryById(tomatoId);
        //Assert
        assertNotNull(result);
        assertEquals(this.savedTomato.getName(), result.getName());
        assertTrue(result.getCategories().contains(this.vegetables));
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
        IngredientUpdateDto tomatoUpdateDto = IngredientUpdateDto.builder()
                .name("chocolate")
                .categoriesId(List.of(2))
                .build();

        when(ingredientRepository.findById(tomatoId)).thenReturn(Optional.of(this.savedTomato));

        this.savedTomato.setName(tomatoUpdateDto.getName());

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(this.savedTomato);
        //Act
        Ingredient result = ingredientService.updateIngredient(tomatoId, tomatoUpdateDto);
        //Assert
        assertNotNull(result);
        assertEquals(tomatoUpdateDto.getName(), result.getName());
        assertTrue(result.getCategoriesId().contains(2));
        verify(ingredientRepository, times(1)).findById(tomatoId);
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    void getAllIngredients() {
        //Arrest
        List<Ingredient> ingredients = List.of(this.savedTomato);
        when(ingredientRepository.findAll()).thenReturn(ingredients);
        //Act
        List<Ingredient> result = ingredientService.getAllIngredients();
        //Assert
        assertNotNull(result);
        assertTrue(result.contains(this.savedTomato));
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    void getAllIngredientsWithCategories() {
        //Arrest
        Integer tomatoId = this.savedTomato.getId();
        Integer vegetablesId = this.vegetables.getId();

        List<Integer> ids = List.of(tomatoId);

        when(ingredientRepository.findAllIds()).thenReturn(ids);

        when(ingredientRepository.findById(tomatoId)).thenReturn(Optional.of(this.savedTomato));

        when(ingredientCategoryClient.getIngredientCategoryById(vegetablesId)).thenReturn(this.vegetables);
        //Act
        List<IngredientResponseDto> result = ingredientService.getAllIngredientsWithCategories();
        //Assert
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(iDto -> iDto.getCategories().contains(this.vegetables)));
        verify(ingredientRepository, times(1)).findAllIds();
    }
}