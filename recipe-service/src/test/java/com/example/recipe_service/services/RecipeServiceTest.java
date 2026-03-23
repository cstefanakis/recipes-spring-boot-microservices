package com.example.recipe_service.services;

import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.dtos.ingredient.IngredientSimpleResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeCreateRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeSimpleResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeUpdateRequestDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.example.recipe_service.dtos.recipeStep.RecipeStepResponseDto;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.models.RecipeIngredient;
import com.example.recipe_service.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.example.recipe_service.enums.Unit.GRAM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private RecipeIngredientService recipeIngredientService;

    @Mock
    private RecipeStepService recipeStepService;

    private Recipe recipe;
    private RecipeIngredient recipeIngredient;
    private Category category;
    private IngredientSimpleResponseDto ingredient;
    private RecipeStepResponseDto recipeStep;

    @BeforeEach
    void setup(){
        this.category = Category.builder()
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
                .name("ingredient name")
                .imgUrl("ingredient img url")
                .build();

        this.recipeStep = RecipeStepResponseDto.builder()
                .id(1)
                .recipeId(1)
                .stepNumber(1)
                .imgUrl("recipe step url")
                .description("recipe step description")
                .build();
    }

    @Test
    void createRecipe() {
        // Arrange
        RecipeCreateRequestDto recipeCreateRequestDto = RecipeCreateRequestDto.builder()
                .title(this.recipe.getTitle())
                .description(this.recipe.getDescription())
                .imgUrl(this.recipe.getImgUrl())
                .categoriesId(List.of(this.category.getId()))
                .build();

        //Mock
        when(categoryService.existsCategoryById(this.category.getId())).thenReturn(true);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(this.recipe);

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        //Act
        recipeService.createRecipe(recipeCreateRequestDto);

        //Verify
        verify(categoryService, times(1)).existsCategoryById(this.category.getId());
        verify(recipeRepository).save(captor.capture());

        Recipe result = captor.getValue();
        //Assert
        assertEquals(this.recipe.getTitle(), result.getTitle());
        assertEquals(this.recipe.getDescription(), result.getDescription());
        assertEquals(this.recipe.getImgUrl(), result.getImgUrl());
    }

    @Test
    void updateRecipe() {
        //Arrest
        RecipeUpdateRequestDto recipeUpdateRequestDto = RecipeUpdateRequestDto.builder()
                .title("pizza")
                .description("pizza from Chris")
                .imgUrl("icon")
                .categoriesId(List.of(1))
                .build();

        List<Category> categories = List.of(this.category);

        //Mock
        when(categoryService.getCategoriesByIds(recipeUpdateRequestDto.getCategoriesId()))
                .thenReturn(categories);
        when(recipeRepository.save(this.recipe))
                .thenReturn(this.recipe);
        //Act
        recipeService.updateRecipe(this.recipe, recipeUpdateRequestDto);
        //Assert
        assertEquals("pizza", this.recipe.getTitle());
        assertEquals("pizza from Chris", this.recipe.getDescription());
        assertEquals("icon", this.recipe.getImgUrl());
        assertEquals(1, this.recipe.getCategories().size());
        //Verify
        verify(categoryService).getCategoriesByIds(recipeUpdateRequestDto.getCategoriesId());
        verify(recipeRepository).save(this.recipe);

    }

    @Test
    void getRecipeById() {
        //Arrest
        Integer recipeId = this.recipe.getId();
        //Mock
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(this.recipe));
        //Act
        Recipe result = recipeService.getRecipeById(recipeId);
        //Assert
        assertNotNull(result);
        assertEquals(this.recipe.getTitle(), result.getTitle());
        assertEquals(this.recipe.getImgUrl(), result.getImgUrl());
        assertEquals(this.recipe.getId(), result.getId());
        //Verify
        verify(recipeRepository).findById(recipeId);
    }

    @Test
    void deleteRecipeById() {
        //Arrest
        Integer recipeId = this.recipe.getId();
        //Mock
        doNothing().when(recipeRepository).deleteById(recipeId);
        //Act
        recipeService.deleteRecipeById(recipeId);
        //Verify
        verify(recipeRepository).deleteById(recipeId);
    }

    @Test
    void getAllSimpleRecipes() {
        //Arrest
        Page<Recipe> recipes = new PageImpl<>(List.of(this.recipe));
        Pageable pageable = PageRequest.of(0, 10);
        //Mock
        when(recipeRepository.findAll(any(Pageable.class))).thenReturn(recipes);
        //Act
        Page<RecipeSimpleResponseDto> result = recipeService.getAllSimpleRecipes(pageable);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream()
                .anyMatch(dto -> dto.getTitle().equals(recipe.getTitle())));
        assertTrue(result.stream()
                .anyMatch(dto -> dto.getImgUrl().equals(recipe.getImgUrl())));
        assertTrue(result.stream()
                .anyMatch(dto -> dto.getDescription().equals(recipe.getDescription())));
        assertTrue(result.stream()
                .anyMatch(dto -> dto.getId().equals(recipe.getId())));
        //Verify
        verify(recipeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void toRecipeResponseDto() {
        //Arrest
        RecipeIngredientResponseDto recipeIngredientResponseDto = RecipeIngredientResponseDto.builder()
                .id(this.recipeIngredient.getId())
                .name(this.ingredient.getName())
                .imgUrl(this.ingredient.getImgUrl())
                .quantity(this.recipeIngredient.getQuantity())
                .unit(this.recipeIngredient.getUnit())
                .build();
        List<RecipeIngredientResponseDto> ingredientsDto = List.of(recipeIngredientResponseDto);

        CategoryResponseDto categoryResponseDto = CategoryResponseDto.builder()
                .id(this.category.getId())
                .name(this.category.getName())
                .imgUrl(this.category.getImgUrl())
                .build();

        List<RecipeStepResponseDto> recipeStepsDto = List.of(this.recipeStep);
        //Mock
        when(recipeIngredientService.getRecipeIngredientsByRecipeId(this.recipe.getId()))
                .thenReturn(ingredientsDto);

        when(categoryService.toCategoryResponseDto(any(Category.class))).thenReturn(categoryResponseDto);

        when(recipeStepService.getRecipeStepsByRecipeId(this.recipe.getId())).thenReturn(recipeStepsDto);
        //Act
        RecipeResponseDto result = recipeService.toRecipeResponseDto(this.recipe);
        //Assert
        assertNotNull(result);
        assertEquals(this.recipe.getId(), result.getId());
        assertEquals(this.recipe.getTitle(), result.getTitle());
        assertEquals(this.recipe.getImgUrl(), result.getImgUrl());
        assertEquals(this.recipe.getDescription(), result.getDescription());
        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().stream()
                .anyMatch(riDto-> riDto.getId().equals(this.ingredient.getId())));
        assertTrue(result.getIngredients().stream()
                .anyMatch(riDto-> riDto.getName().equals(this.ingredient.getName())));
        assertTrue(result.getIngredients().stream()
                .anyMatch(riDto-> riDto.getImgUrl().equals(this.ingredient.getImgUrl())));
        assertTrue(result.getIngredients().stream()
                .anyMatch(riDto-> riDto.getUnit().equals(this.recipeIngredient.getUnit())));
        assertTrue(result.getIngredients().stream()
                .anyMatch(riDto-> riDto.getQuantity().equals(this.recipeIngredient.getQuantity())));

        assertNotNull(result.getCategories());
        assertTrue(result.getCategories().stream()
                .anyMatch(cDto -> cDto.getId().equals(this.category.getId())));
        assertTrue(result.getCategories().stream()
                .anyMatch(cDto -> cDto.getName().equals(this.category.getName())));
        assertTrue(result.getCategories().stream()
                .anyMatch(cDto -> cDto.getImgUrl().equals(this.category.getImgUrl())));

        assertNotNull(result.getRecipeSteps());
        assertTrue(result.getRecipeSteps().stream()
                .anyMatch(rsDto -> rsDto.getId().equals(this.recipeStep.getId())));
        assertTrue(result.getRecipeSteps().stream()
                .anyMatch(rsDto -> rsDto.getRecipeId().equals(this.recipeStep.getRecipeId())));
        assertTrue(result.getRecipeSteps().stream()
                .anyMatch(rsDto -> rsDto.getImgUrl().equals(this.recipeStep.getImgUrl())));
        assertTrue(result.getRecipeSteps().stream()
                .anyMatch(rsDto -> rsDto.getDescription().equals(this.recipeStep.getDescription())));
        //Verify
        verify(recipeIngredientService).getRecipeIngredientsByRecipeId(this.recipe.getId());

        verify(categoryService).toCategoryResponseDto(any(Category.class));

        verify(recipeStepService).getRecipeStepsByRecipeId(this.recipe.getId());
    }

    @Test
    void getRecipesByCategoryId() {
        //Arrest
        Integer categoryId = this.category.getId();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recipe> recipes = new PageImpl<>(List.of(this.recipe));
        //Mock
        when(recipeRepository.findRecipesByCategoryId(categoryId, pageable)).thenReturn(recipes);
        //Act
        Page<RecipeSimpleResponseDto> result = recipeService.getRecipesByCategoryId(categoryId, pageable);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream()
                .anyMatch(rDto -> rDto.getId().equals(this.recipe.getId())));
        assertTrue(result.stream()
                .anyMatch(rDto -> rDto.getTitle().equals(this.recipe.getTitle())));
        assertTrue(result.stream()
                .anyMatch(rDto -> rDto.getImgUrl().equals(this.recipe.getImgUrl())));
        assertTrue(result.stream()
                .anyMatch(rDto -> rDto.getDescription().equals(this.recipe.getDescription())));
        //Verify
        verify(recipeRepository).findRecipesByCategoryId(categoryId, pageable);
    }
}