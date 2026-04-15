package com.example.recipe_service.services;

import com.example.recipe_service.clients.IngredientClient;
import com.example.recipe_service.dtos.ingredient.IngredientSimpleResponseDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientCreateRequestDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientUpdateRequestDto;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.models.RecipeIngredient;
import com.example.recipe_service.repositories.RecipeIngredientRepository;
import com.example.recipe_service.repositories.RecipeRepository;
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

import static com.example.recipe_service.enums.Unit.GRAM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RecipeIngredientServiceTest {

    @InjectMocks
    private RecipeIngredientService recipeIngredientService;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientClient ingredientClient;

    @Mock
    private UserService userService;

    private Recipe recipe;
    private RecipeIngredient recipeIngredient;
    private IngredientSimpleResponseDto ingredient;
    private RecipeIngredientUpdateRequestDto recipeIngredientUpdateRequestDto;
    private IngredientSimpleResponseDto ingredientSimpleDto;

    @BeforeEach
    void setup(){
        Category category = Category.builder()
                .id(1)
                .name("Category")
                .imgUrl("url")
                .build();

        this.recipe = Recipe.builder()
                .id(1)
                .title("title")
                .description("description")
                .imgUrl("url")
                .categories(List.of(category))
                .userId(2)
                .build();

        this.recipeIngredient = RecipeIngredient.builder()
                .id(1)
                .ingredientId(1)
                .unit(GRAM)
                .quantity(500.0)
                .recipe(this.recipe)
                .build();

        this.ingredient = IngredientSimpleResponseDto.builder()
                .id(1)
                .name("ingredient")
                .imgUrl("url")
                .build();

        this.recipeIngredientUpdateRequestDto = RecipeIngredientUpdateRequestDto.builder()
                .ingredientId(1)
                .quantity(10.0)
                .unit(GRAM)
                .build();

        this.ingredientSimpleDto = IngredientSimpleResponseDto.builder()
                .imgUrl(this.ingredient.getImgUrl())
                .name(this.ingredient.getName())
                .id(this.ingredient.getId())
                .build();
    }

    @Test
    void createRecipeIngredient() {
        //Arrange
        Integer recipeId = this.recipe.getId();
        Integer ingredientId = this.ingredient.getId();
        Integer ownerId = this.recipe.getUserId();

        RecipeIngredientCreateRequestDto recipeIngredientCreateRequestDto = RecipeIngredientCreateRequestDto.builder()
                .ingredientId(1)
                .recipeId(this.recipe.getId())
                .quantity(this.recipeIngredient.getQuantity())
                .unit(this.recipeIngredient.getUnit())
                .build();
        //Mock
        when(ingredientClient.ingredientExistById(ingredientId)).thenReturn(ingredientId);
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(this.recipe));
        when(recipeIngredientRepository.save(any(RecipeIngredient.class))).thenReturn(this.recipeIngredient);
        when(recipeRepository.findRecipeOwnerIdByRecipeId(recipeId)).thenReturn(ownerId);
        when(userService.isOwnerOrAdmin(ownerId)).thenReturn(true);
        //Act
        recipeIngredientService.createRecipeIngredient(recipeIngredientCreateRequestDto);
        //Verify
        verify(ingredientClient).ingredientExistById(ingredientId);
        verify(recipeRepository).findRecipeOwnerIdByRecipeId(recipeId);
        verify(userService).isOwnerOrAdmin(ownerId);
        verify(recipeRepository).findById(recipeId);
        verify(recipeIngredientRepository).save(any(RecipeIngredient.class));
    }

    @Test
    void createRecipeIngredient_noOwnerOrAdmin() {
        //Arrange
        Integer recipeId = this.recipe.getId();
        Integer ownerId = this.recipe.getUserId();

        RecipeIngredientCreateRequestDto recipeIngredientCreateRequestDto = RecipeIngredientCreateRequestDto.builder()
                .ingredientId(1)
                .recipeId(this.recipe.getId())
                .quantity(this.recipeIngredient.getQuantity())
                .unit(this.recipeIngredient.getUnit())
                .build();

        //Mock
        when(recipeRepository.findRecipeOwnerIdByRecipeId(recipeId)).thenReturn(ownerId);
        when(userService.isOwnerOrAdmin(ownerId)).thenReturn(false);

        //Act and Assert
        assertThrows(RuntimeException.class, () -> recipeIngredientService
                .createRecipeIngredient(recipeIngredientCreateRequestDto));

        //Verify
        verify(recipeRepository, times(1))
                .findRecipeOwnerIdByRecipeId(recipeId);
        verify(userService, times(1))
                .isOwnerOrAdmin(ownerId);
    }

    @Test
    void getRecipeIngredientsByRecipeId() {
        //Arrange
        List<RecipeIngredient> recipeIngredients = List.of(this.recipeIngredient);
        Integer recipeId = this.recipe.getId();
        //Mock
        when(recipeIngredientRepository.findRecipeIngredientsByRecipeId(recipeId))
                .thenReturn(recipeIngredients);
        when(ingredientClient.getIngredientById(this.recipeIngredient.getIngredientId()))
                .thenReturn(this.ingredient);
        //Act
        List<RecipeIngredientResponseDto> result = recipeIngredientService.getRecipeIngredientsByRecipeId(recipeId);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getName().equals(this.ingredient.getName())));
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getImgUrl().equals(this.ingredient.getImgUrl())));
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getQuantity().equals(this.recipeIngredient.getQuantity())));
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getUnit().equals(this.recipeIngredient.getUnit())));
        //Verify
        verify(recipeIngredientRepository).findRecipeIngredientsByRecipeId(recipeId);
    }

    @Test
    void deleteRecipeIngredientById() {
        //Arrange
        Integer recipeIngredientId = this.recipeIngredient.getId();

        Integer ownerId = this.recipe.getUserId();

        //Mock
        when(recipeIngredientRepository.getRecipeOwnerByRecipeIngredientId(recipeIngredientId))
                .thenReturn(ownerId);
        when(userService.isOwnerOrAdmin(ownerId))
                .thenReturn(true);
        doNothing().when(recipeIngredientRepository)
                .deleteById(recipeIngredientId);

        //Act
        recipeIngredientService.deleteRecipeIngredientById(recipeIngredientId);
        //Verify
        verify(recipeIngredientRepository)
                .getRecipeOwnerByRecipeIngredientId(recipeIngredientId);
        verify(userService)
                .isOwnerOrAdmin(ownerId);
        verify(recipeIngredientRepository)
                .deleteById(recipeIngredientId);
    }

    @Test
    void deleteRecipeIngredientById_notOwnerOrAdmin() {
        //Arrange
        Integer recipeIngredientId = this.recipeIngredient.getId();

        Integer ownerId = this.recipe.getUserId();

        //Mock
        when(recipeIngredientRepository.getRecipeOwnerByRecipeIngredientId(recipeIngredientId))
                .thenReturn(ownerId);
        when(userService.isOwnerOrAdmin(ownerId))
                .thenReturn(false);

        //Act and Assert
        assertThrows(RuntimeException.class,
                () -> recipeIngredientService.deleteRecipeIngredientById(recipeIngredientId));

        //Verify
        verify(recipeIngredientRepository)
                .getRecipeOwnerByRecipeIngredientId(recipeIngredientId);
        verify(userService)
                .isOwnerOrAdmin(ownerId);
    }

    @Test
    void getRecipeIngredientById() {
        //Arrange
        Integer recipeIngredientId = this.recipeIngredient.getId();
        //Mock
        when(recipeIngredientRepository.findById(recipeIngredientId)).thenReturn(Optional.of(this.recipeIngredient));
        //Act
        RecipeIngredient result = recipeIngredientService.getRecipeIngredientById(recipeIngredientId);
        //Assert
        assertNotNull(result);
        assertEquals(this.recipeIngredient.getIngredientId(), result.getIngredientId());
        assertEquals(this.recipeIngredient.getId(), result.getId());
        assertEquals(this.recipeIngredient.getUnit(), result.getUnit());
        assertEquals(this.recipeIngredient.getRecipe(), result.getRecipe());
        assertEquals(this.recipeIngredient.getQuantity(), result.getQuantity());
        //Verify
        verify(recipeIngredientRepository).findById(recipeIngredientId);
    }

    @Test
    void updateRecipeIngredient() {

        //Arrange
        Integer recipeIngredientId = this.recipeIngredient.getId();
        Integer ownerId = this.recipe.getUserId();

        //Mock
        when(recipeIngredientRepository.getRecipeOwnerByRecipeIngredientId(recipeIngredientId))
                .thenReturn(ownerId);
        when( userService.isOwnerOrAdmin(ownerId))
                .thenReturn(true);
        when(ingredientClient.ingredientExistById(any(Integer.class)))
                .thenReturn(this.ingredient.getId());
        when(recipeIngredientRepository.save(any(RecipeIngredient.class)))
                .thenReturn(this.recipeIngredient);

        //Act
        recipeIngredientService.updateRecipeIngredient(this.recipeIngredient, recipeIngredientUpdateRequestDto);

        //Assert
        assertEquals(this.recipeIngredient.getQuantity(), recipeIngredientUpdateRequestDto.getQuantity());
        assertEquals(this.recipeIngredient.getIngredientId(), recipeIngredientUpdateRequestDto.getIngredientId());
        assertEquals(this.recipeIngredient.getUnit(), recipeIngredientUpdateRequestDto.getUnit());

        //Verify
        verify(recipeIngredientRepository)
                .getRecipeOwnerByRecipeIngredientId(recipeIngredient.getId());
        verify(userService)
                .isOwnerOrAdmin(ownerId);
        verify(ingredientClient)
                .ingredientExistById(any(Integer.class));
        verify(recipeIngredientRepository)
                .save(any(RecipeIngredient.class));
    }

    @Test
    void updateRecipeIngredient_notOwnerOrAdmin() {

        //Arrange
        Integer recipeIngredientId = this.recipeIngredient.getId();
        Integer ownerId = this.recipe.getUserId();

        //Mock
        when(recipeIngredientRepository.getRecipeOwnerByRecipeIngredientId(recipeIngredientId))
                .thenReturn(ownerId);
        when( userService.isOwnerOrAdmin(ownerId))
                .thenReturn(false);

        //Act and Assert
        assertThrows(RuntimeException.class,
                () -> recipeIngredientService.updateRecipeIngredient(this.recipeIngredient, recipeIngredientUpdateRequestDto));

        //Verify
        verify(recipeIngredientRepository)
                .getRecipeOwnerByRecipeIngredientId(recipeIngredient.getId());
        verify(userService)
                .isOwnerOrAdmin(ownerId);
    }

    @Test
    void updateRecipeIngredient_IngredientNotExists() {
        //Arrange
        Integer recipeIngredientId = this.recipeIngredient.getId();
        Integer ownerId = this.recipe.getUserId();

        //Mock
        when(recipeIngredientRepository.getRecipeOwnerByRecipeIngredientId(recipeIngredientId))
                .thenReturn(ownerId);
        when( userService.isOwnerOrAdmin(ownerId))
                .thenReturn(true);
        when(ingredientClient.ingredientExistById(any(Integer.class)))
                .thenThrow(EntityNotFoundException.class);

        //Act
        assertThrows(EntityNotFoundException.class, () ->
            recipeIngredientService.updateRecipeIngredient(this.recipeIngredient, recipeIngredientUpdateRequestDto)
        );

        //Verify
        verify(recipeIngredientRepository)
                .getRecipeOwnerByRecipeIngredientId(recipeIngredient.getId());
        verify(userService)
                .isOwnerOrAdmin(ownerId);
        verify(ingredientClient)
                .ingredientExistById(any(Integer.class));
    }

    @Test
    void recipeIngredientWithIngredientIdExists() {
        //Arrange
        Integer ingredientId = this.recipeIngredient.getIngredientId();
        //Mock
        when(recipeIngredientRepository.existsWithIngredientId(ingredientId)).thenReturn(true);
        //Act
        boolean result = recipeIngredientService.recipeIngredientWithIngredientIdExists(ingredientId);
        //Assert
        assertTrue(result);
    }

    @Test
    void getAllRecipeIngredients() {
        //Arrange
        List<RecipeIngredient> recipeIngredients = List.of(this.recipeIngredient);

        //Mock
        when(recipeIngredientRepository.findAll())
                .thenReturn(recipeIngredients);

        //Act
        List<RecipeIngredient> result = recipeIngredientService.getAllRecipeIngredients();

        //Assert
        assertNotNull(result);
        assertTrue(result.contains(this.recipeIngredient));

        //Verify
        verify(recipeIngredientRepository, times(1))
                .findAll();
    }

    @Test
    void toRecipeIngredientResponseDto() {
        //Arrange
        Integer recipeIngredientId = this.recipeIngredient.getIngredientId();
        Integer ingredientId = this.ingredient.getId();

        //Mock
        when(ingredientClient.ingredientExistById(recipeIngredientId))
                .thenReturn(ingredientId);
        when(ingredientClient.getIngredientById(ingredientId))
                .thenReturn(this.ingredientSimpleDto);

        //Act
        RecipeIngredientResponseDto result =
                recipeIngredientService.toRecipeIngredientResponseDto(this.recipeIngredient);

        //Assert
        assertNotNull(result);
        assertEquals(this.recipeIngredient.getIngredientId(), result.getId());
        assertEquals(this.recipeIngredient.getUnit(), result.getUnit());
        assertEquals(this.ingredient.getName(), result.getName());
        assertEquals(this.ingredient.getImgUrl(), result.getImgUrl());
        assertEquals(this.recipeIngredient.getQuantity(), result.getQuantity());

        //Verify
        verify(ingredientClient, times(1))
                .ingredientExistById(recipeIngredientId);
        verify(ingredientClient, times(1))
                .getIngredientById(ingredientId);
    }

    @Test
    void getRecipeIngredients() {
        //Arrange
        Integer recipeIngredientId = this.recipeIngredient.getIngredientId();
        Integer ingredientId = this.ingredient.getId();
        List<RecipeIngredient> recipeIngredients = List.of(this.recipeIngredient);

        //Mock
        when(recipeIngredientRepository.findAll())
                .thenReturn(recipeIngredients);
        when(ingredientClient.ingredientExistById(recipeIngredientId))
                .thenReturn(ingredientId);
        when(ingredientClient.getIngredientById(ingredientId))
                .thenReturn(ingredientSimpleDto);

        //Act
        List<RecipeIngredientResponseDto> result =
                recipeIngredientService.getRecipeIngredients();

        //Assert
        assertNotNull(result);
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getQuantity().equals(this.recipeIngredient.getQuantity())));
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getUnit().equals(this.recipeIngredient.getUnit())));
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getImgUrl().equals(this.ingredient.getImgUrl())));
        assertTrue(result.stream()
                .anyMatch(ri -> ri.getName().equals(this.ingredient.getName())));

        //Verify
        verify(recipeIngredientRepository, times(1))
                .findAll();
        verify(ingredientClient, times(1))
                .ingredientExistById(recipeIngredientId);
        verify(ingredientClient, times(1))
                .getIngredientById(ingredientId);
    }
}