package com.recipe.recipe.services;

import com.recipe.recipe.clients.RecipeStepClient;
import com.recipe.recipe.dtos.category.CategoryResponseDto;
import com.recipe.recipe.dtos.ingredient.IngredientSimpleResponseDto;
import com.recipe.recipe.dtos.recipe.RecipeCreateRequestDto;
import com.recipe.recipe.dtos.recipe.RecipeResponseDto;
import com.recipe.recipe.dtos.recipe.RecipeSimpleResponseDto;
import com.recipe.recipe.dtos.recipe.RecipeUpdateRequestDto;
import com.recipe.recipe.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.recipe.recipe.dtos.recipeStep.RecipeStepResponseDto;
import com.recipe.recipe.dtos.user.UserResponseIdAndRole;
import com.recipe.recipe.models.Category;
import com.recipe.recipe.models.Recipe;
import com.recipe.recipe.models.RecipeIngredient;
import com.recipe.recipe.repositories.RecipeRepository;
import jakarta.persistence.EntityExistsException;
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

import static com.recipe.recipe.enums.Unit.GRAM;
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

    @Mock
    private RecipeStepClient recipeStepClient;

    @Mock
    private UserService userService;

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

        UserResponseIdAndRole authenticatedUser = UserResponseIdAndRole.builder()
                .role("USER")
                .id(1)
                .build();

        //Mock
        when(userService.getAuthenticatedUser())
                .thenReturn(authenticatedUser);
        when(recipeRepository.titleExists(any(String.class)))
                .thenReturn(false);
        when(recipeRepository.save(any(Recipe.class)))
                .thenReturn(this.recipe);

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        //Act
        recipeService.createRecipe(recipeCreateRequestDto);

        //Verify
        verify((userService), times(1))
                .getAuthenticatedUser();
        verify(recipeRepository)
                .titleExists(any(String.class));
        verify(recipeRepository)
                .save(captor.capture());

        Recipe result = captor.getValue();

        //Assert
        assertEquals(this.recipe.getTitle(), result.getTitle());
        assertEquals(1, result.getUserId());
        assertEquals(this.recipe.getDescription(), result.getDescription());
        assertEquals(this.recipe.getImgUrl(), result.getImgUrl());
    }

    @Test
    void createRecipe_TitleExist() {
        // Arrange
        RecipeCreateRequestDto recipeCreateRequestDto = RecipeCreateRequestDto.builder()
                .title(this.recipe.getTitle())
                .description(this.recipe.getDescription())
                .imgUrl(this.recipe.getImgUrl())
                .categoriesId(List.of(this.category.getId()))
                .build();

        UserResponseIdAndRole authenticatedUser = UserResponseIdAndRole.builder()
                .role("USER")
                .id(1)
                .build();

        //Mock
        when(userService.getAuthenticatedUser())
                .thenReturn(authenticatedUser);
        when(recipeRepository.titleExists(any(String.class)))
                .thenReturn(true);

        //Act
        assertThrows(EntityExistsException.class, () ->
            recipeService.createRecipe(recipeCreateRequestDto));

        //Verify
        verify((userService), times(1))
                .getAuthenticatedUser();
        verify(recipeRepository)
                .titleExists(any(String.class));
    }

    @Test
    void updateRecipe() {
        //Arrange
        RecipeUpdateRequestDto recipeUpdateRequestDto = RecipeUpdateRequestDto.builder()
                .title("pizza")
                .description("pizza from Chris")
                .imgUrl("icon")
                .categoriesId(List.of(1))
                .build();

        List<Category> categories = List.of(this.category);

        Integer recipeOwnerId = this.recipe.getUserId();

        //Mock
        when(userService.isOwnerOrAdmin(recipeOwnerId))
                .thenReturn(true);
        when(recipeRepository.titleExists(any(String.class)))
                .thenReturn(false);
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
        verify(userService , times(1))
                .isOwnerOrAdmin(recipeOwnerId);
        verify(recipeRepository, times(1))
                .titleExists(any(String.class));
        verify(categoryService)
                .getCategoriesByIds(recipeUpdateRequestDto.getCategoriesId());
        verify(recipeRepository)
                .save(this.recipe);
    }

    @Test
    void updateRecipe_noOwnerOrAdmin() {
        //Arrange
        RecipeUpdateRequestDto recipeUpdateRequestDto = RecipeUpdateRequestDto.builder()
                .title("pizza")
                .description("pizza from Chris")
                .imgUrl("icon")
                .categoriesId(List.of(1))
                .build();

        Integer recipeOwnerId = this.recipe.getUserId();

        //Mock
        when(userService.isOwnerOrAdmin(recipeOwnerId))
                .thenReturn(false);
        //Act and Assert
        assertThrows(RuntimeException.class,
                () -> recipeService.updateRecipe(this.recipe, recipeUpdateRequestDto));

        //Verify
        verify(userService , times(1))
                .isOwnerOrAdmin(recipeOwnerId);
    }

    @Test
    void updateRecipe_TitleExists() {
        //Arrange
        RecipeUpdateRequestDto recipeUpdateRequestDto = RecipeUpdateRequestDto.builder()
                .title("pizza")
                .description("pizza from Chris")
                .imgUrl("icon")
                .categoriesId(List.of(1))
                .build();

        Integer recipeOwnerId = this.recipe.getUserId();

        //Mock
        when(userService.isOwnerOrAdmin(recipeOwnerId))
                .thenReturn(true);
        when(recipeRepository.titleExists(any(String.class)))
                .thenReturn(true);
        //Act
        assertThrows(EntityExistsException.class, ()->
            recipeService.updateRecipe(this.recipe, recipeUpdateRequestDto));

        //Verify
        verify(userService , times(1))
                .isOwnerOrAdmin(recipeOwnerId);
        verify(recipeRepository, times(1))
                .titleExists(any(String.class));
    }

    @Test
    void getRecipeById() {
        //Arrange
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

        Integer ownerId = this.recipe.getUserId();

        //Mock
        when(recipeRepository.findRecipeOwnerIdByRecipeId(recipeId))
                .thenReturn(ownerId);
        when(userService.isOwnerOrAdmin(ownerId))
                .thenReturn(true);
        doNothing().when(recipeStepClient)
                .deleteAllByRecipeId(recipeId);
        doNothing().when(recipeRepository)
                .deleteById(recipeId);

        //Act
        recipeService.deleteRecipeById(recipeId);

        //Verify
        verify(recipeRepository)
                .findRecipeOwnerIdByRecipeId(recipeId);
        verify(userService, times(1))
                .isOwnerOrAdmin(ownerId);
        verify(recipeStepClient)
                .deleteAllByRecipeId(recipeId);
        verify(recipeRepository)
                .deleteById(recipeId);
    }

    @Test
    void deleteRecipeById_noOwnerOrAdmin() {
        //Arrest
        Integer recipeId = this.recipe.getId();

        Integer ownerId = this.recipe.getUserId();

        //Mock
        when(recipeRepository.findRecipeOwnerIdByRecipeId(recipeId))
                .thenReturn(ownerId);
        when(userService.isOwnerOrAdmin(ownerId))
                .thenReturn(false);

        //Act and Assert
        assertThrows(RuntimeException.class,
                () -> recipeService.deleteRecipeById(recipeId));

        //Verify
        verify(recipeRepository, times(1))
                .findRecipeOwnerIdByRecipeId(recipeId);
        verify(userService, times(1))
                .isOwnerOrAdmin(ownerId);
    }

    @Test
    void getAllSimpleRecipes() {
        //Arrange
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
        //Arrange
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
        //Arrange
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

    @Test
    void recipeExists() {
        //Arrange
        Integer recipeId = this.recipe.getId();
        //Mock
        when(recipeRepository.existsById(recipeId)).thenReturn(true);
        //Act
        boolean result = recipeService.recipeExists(recipeId);
        //Assert
        assertTrue(result);
        //Verify
        verify(recipeRepository, times(1)).existsById(recipeId);
    }

    @Test
    void getRecipeOwnerIdByRecipeId() {
        //Arrange
        Integer recipeId = this.recipe.getId();
        Integer recipeUserId = this.recipe.getUserId();

        //Mock
        when(recipeRepository.findRecipeOwnerIdByRecipeId(recipeId))
                .thenReturn(recipeUserId);

        //Act
        Integer result = recipeService.getRecipeOwnerIdByRecipeId(recipeId);

        //Assert
        assertNotNull(result);
        assertEquals(recipeUserId, result);

        //Verify
        verify(recipeRepository, times(1))
                .findRecipeOwnerIdByRecipeId(recipeId);
    }
}