package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.recipe.RecipeCreateRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeSimpleResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeUpdateRequestDto;
import com.example.recipe_service.jwt.JwtFilter;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    @MockitoBean
    private JwtFilter jwtFilter;

    private RecipeSimpleResponseDto recipeSimpleResponseDto;
    private Recipe recipe;

    @BeforeEach
    void setup(){
        Category category = Category.builder()
                .name("category")
                .imgUrl("url")
                .build();

        this.recipeSimpleResponseDto = RecipeSimpleResponseDto.builder()
                .id(1)
                .title("pizza")
                .description("my pizza")
                .build();

        this.recipe = Recipe.builder()
                .id(1)
                .title("pizza")
                .description("my pizza")
                .categories(List.of(category))
                .build();
    }

    @Test
    void createRecipe() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "title" : "pizza",
                    "description" : "chris pizza",
                    "imgUrl" : "http://img.png",
                    "categoriesId" : [1]
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());

        ArgumentCaptor<RecipeCreateRequestDto> captor = ArgumentCaptor
                .forClass(RecipeCreateRequestDto.class);
        //Verify
        verify(recipeService).createRecipe(captor.capture());

        RecipeCreateRequestDto createDto = captor.getValue();

        //Assert
        assertNotNull(createDto);
        assertEquals("pizza", createDto.getTitle());
        assertEquals("chris pizza", createDto.getDescription());
        assertEquals("http://img.png", createDto.getImgUrl());
        assertTrue(createDto.getCategoriesId().contains(1));
    }

    @Test
    void createRecipe_nullTitle() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "description" : "chris pizza",
                    "imgUrl" : "img url",
                    "categoriesId" : [1]
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipe_nullDescription() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "title" : "pizza",
                    "imgUrl" : "img url",
                    "categoriesId" : [1]
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipe_nullImgUrl() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "title" : "pizza",
                    "description" : "chris pizza",
                    "categoriesId" : [1]
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void createRecipe_nullCategoryId() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "title" : "pizza",
                    "description" : "chris pizza",
                    "imgUrl" : "img url"
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRecipeById() throws Exception {
        //Arrange
        Page<RecipeSimpleResponseDto> recipes = new PageImpl<>(List.of(this.recipeSimpleResponseDto));

        //Mock
        when(recipeService.getAllSimpleRecipes(any(Pageable.class)))
                .thenReturn(recipes);
        //Preform Get
        mockMvc.perform(get("/api/recipes")
                .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].title")
                        .value(this.recipeSimpleResponseDto.getTitle()))
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].description")
                        .value(this.recipeSimpleResponseDto.getDescription()))
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].imgUrl")
                        .value(this.recipeSimpleResponseDto.getImgUrl()))
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].id")
                        .value(this.recipeSimpleResponseDto.getId()));
        //Verify
        verify(recipeService).getAllSimpleRecipes(any(Pageable.class));
    }

    @Test
    void getAllRecipes() throws Exception {
        //Arrange
        Page<RecipeSimpleResponseDto> recipes = new PageImpl<>(List.of(this.recipeSimpleResponseDto));
        //Mock
        when(recipeService.getAllSimpleRecipes(any(Pageable.class))).thenReturn(recipes);
        //Preform Get
        mockMvc.perform(get("/api/recipes")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].title")
                        .value(this.recipeSimpleResponseDto.getTitle()))
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].description")
                        .value(this.recipeSimpleResponseDto.getDescription()))
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].imgUrl")
                        .value(this.recipeSimpleResponseDto.getImgUrl()))
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].id")
                        .value(this.recipeSimpleResponseDto.getId()));
        //Verify
        verify(recipeService).getAllSimpleRecipes(any(Pageable.class));
    }

    @Test
    void getRecipesByCategoryId() throws Exception {
        //Arrange
        Integer categoryId = 1;
        Page<RecipeSimpleResponseDto> recipes = new PageImpl<>(List.of(this.recipeSimpleResponseDto));
        //Mock
        when(recipeService.getRecipesByCategoryId(eq(categoryId), any(Pageable.class))).thenReturn(recipes);
        //Perform
        mockMvc.perform(get("/api/recipes/by-category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].title")
                        .value(this.recipeSimpleResponseDto.getTitle()))
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].description")
                        .value(this.recipeSimpleResponseDto.getDescription()))
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].imgUrl")
                        .value(this.recipeSimpleResponseDto.getImgUrl()))
                .andExpect(jsonPath("$._embedded.recipeSimpleResponseDtoList[0].id")
                        .value(this.recipeSimpleResponseDto.getId()));
        //Verify
        verify(recipeService).getRecipesByCategoryId(eq(categoryId), any(Pageable.class));
    }

    @Test
    void updateRecipeById() throws Exception {
        //Arrange
        Integer recipeId = 1;
        String requestBody = """
                {
                    "title" : "pizza",
                    "description" : "chris pizza",
                    "imgUrl" : "img url",
                    "categoriesId" : [1]
                }
                """;
        //Mock
        when(recipeService.getRecipeById(recipeId)).thenReturn(this.recipe);
        doNothing().when(recipeService).updateRecipe(any(Recipe.class), any(RecipeUpdateRequestDto.class));
        //Perform Put
        mockMvc.perform(put("/api/recipes/{recipeId}", recipeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
        //Verify
        verify(recipeService).getRecipeById(recipeId);

        ArgumentCaptor<RecipeUpdateRequestDto> captor = ArgumentCaptor
                .forClass(RecipeUpdateRequestDto.class);

        //Verify
        verify(recipeService).updateRecipe(any(Recipe.class), captor.capture());
        RecipeUpdateRequestDto createDto = captor.getValue();

        //Assert
        assertNotNull(createDto);
        assertEquals("pizza", createDto.getTitle());
        assertEquals("chris pizza", createDto.getDescription());
        assertEquals("img url", createDto.getImgUrl());
        assertTrue(createDto.getCategoriesId().contains(1));
    }

    @Test
    void deleteRecipeById() throws Exception {
        //Arrange
        Integer recipeId = 1;
        //Mock
        doNothing().when(recipeService).deleteRecipeById(recipeId);
        //Perform delete
        mockMvc.perform(delete("/api/recipes/{recipeId}", recipeId))
                .andExpect(status().isNoContent());
        // Verify
        verify(recipeService).deleteRecipeById(recipeId);
    }

    @Test
    void recipeExists() throws Exception {
        //Arrange
        Integer recipeId = this.recipe.getId();
        //Mock
        when(recipeService.recipeExists(recipeId)).thenReturn(true);
        //Perform
        mockMvc.perform(get("/api/recipes/exists/{recipeId}", recipeId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        //Verify
        verify(recipeService).recipeExists(recipeId);
    }
}