package com.recipe.ingredients.controllers;

import com.recipe.ingredients.dtos.category.CategoryResponseDto;
import com.recipe.ingredients.dtos.ingredient.IngredientCreateRequestDto;
import com.recipe.ingredients.dtos.ingredient.IngredientResponseDto;
import com.recipe.ingredients.dtos.ingredient.IngredientSimpleResponseDto;
import com.recipe.ingredients.dtos.ingredient.IngredientUpdateRequestDto;
import com.recipe.ingredients.jwt.JwtFilter;
import com.recipe.ingredients.models.Category;
import com.recipe.ingredients.models.Ingredient;
import com.recipe.ingredients.services.IngredientService;
import jakarta.persistence.EntityNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IngredientController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientService ingredientService;

    @MockitoBean
    private JwtFilter jwtFilter;

    private IngredientSimpleResponseDto tomatoDto;

    @BeforeEach
    void setup(){
        this.tomatoDto = IngredientSimpleResponseDto.builder()
                .id(1)
                .name("Tomato")
                .imgUrl("https://example.com/img.png")
                .build();
    }

    @Test
    void createIngredient() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "name" : "Tomato",
                    "imgUrl" : "https://example.com/img.png",
                    "categoriesId" : [1]
                }
                """;
        //Mock
        doNothing().when(ingredientService).createIngredient(any(IngredientCreateRequestDto.class));

        //Perform Post
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        ArgumentCaptor<IngredientCreateRequestDto> captor = ArgumentCaptor.forClass(IngredientCreateRequestDto.class);
        //Verify
        verify(ingredientService).createIngredient(captor.capture());

        IngredientCreateRequestDto ingredientDto = captor.getValue();

        //Assert
        assertNotNull(ingredientDto);
        assertEquals("Tomato", ingredientDto.getName());
        assertEquals("https://example.com/img.png", ingredientDto.getImgUrl());
        assertEquals(1, ingredientDto.getCategoriesId().getFirst());
    }

    @Test
    void createIngredient_nullArgument() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "categoriesId" : [1]
                }
                """;
        //Mock
        doNothing().when(ingredientService).createIngredient(any(IngredientCreateRequestDto.class));

        //Perform Post
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getIngredientWithCategoryById() throws Exception {
        //Arrest
        CategoryResponseDto category = CategoryResponseDto.builder()
                .id(1)
                .imgUrl("https://example.com/img.png")
                .name("Vegetables")
                .build();
        IngredientResponseDto ingredientResponseDto = IngredientResponseDto.builder()
                .id(1)
                .name("Tomato")
                .categories(List.of(category))
                .build();
        //Mock Service
        when(ingredientService.getIngredientWithCategoryById(eq(1)))
                .thenReturn(ingredientResponseDto);
        //Perform get
        mockMvc.perform(get("/api/ingredients/with-categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ingredientResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(ingredientResponseDto.getName()))
                .andExpect(jsonPath("$.categories[0].name").value(ingredientResponseDto.getCategories().getFirst().getName()))
                .andExpect(jsonPath("$.categories[0].id").value(ingredientResponseDto.getCategories().getFirst().getId()))
                .andExpect(jsonPath("$.categories[0].imgUrl").value(ingredientResponseDto.getCategories().getFirst().getImgUrl()));
        //Verify
        verify(ingredientService).getIngredientWithCategoryById(1);
    }

    @Test
    void deleteIngredient() throws Exception {
        //Mock Service
        doNothing().when(ingredientService).deleteIngredient(eq(1));
        //Perform delete
        mockMvc.perform(delete("/api/ingredients/1"))
                .andExpect(status().isNoContent());
        //Verify
        verify(ingredientService).deleteIngredient(1);
    }

    @Test
    void deleteIngredient_invalidId() throws Exception {
        //Mock Service
        doThrow(new EntityNotFoundException()).when(ingredientService).deleteIngredient(1);
        //Perform delete
        mockMvc.perform(delete("/api/ingredients/1"))
                .andExpect(status().isNotFound());
        //Verify
        verify(ingredientService).deleteIngredient(1);
    }

    @Test
    void getAllSimpleIngredients() throws Exception {
        //Arrest
        Page<IngredientSimpleResponseDto> ingredients = new PageImpl<>(List.of(this.tomatoDto));
        //Mock
        when(ingredientService.getAllSimpleIngredients(any(Pageable.class))).thenReturn(ingredients);
        //Perform Get
        mockMvc.perform(get("/api/ingredients")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ingredientSimpleResponseDtoList[0].id").value(this.tomatoDto.getId()))
                .andExpect(jsonPath("$._embedded.ingredientSimpleResponseDtoList[0].name").value(this.tomatoDto.getName()))
                .andExpect(jsonPath("$._embedded.ingredientSimpleResponseDtoList[0].imgUrl").value(this.tomatoDto.getImgUrl()));
        //Verify
        verify(ingredientService, times(1)).getAllSimpleIngredients(any(Pageable.class));
    }

    @Test
    void getAllSimpleIngredientsByCategoryId() throws Exception {
        //Arrest
        Integer categoryId = 1;
        Page<IngredientSimpleResponseDto> ingredients = new PageImpl<>(List.of(this.tomatoDto));
        //Mock
        when(ingredientService.getAllSimpleIngredientsByCategoryId(eq(categoryId), any(Pageable.class))).thenReturn(ingredients);
        //Perform Get
        mockMvc.perform(get("/api/ingredients/by-category/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ingredientSimpleResponseDtoList[0].id").value(this.tomatoDto.getId()))
                .andExpect(jsonPath("$._embedded.ingredientSimpleResponseDtoList[0].name").value(this.tomatoDto.getName()))
                .andExpect(jsonPath("$._embedded.ingredientSimpleResponseDtoList[0].imgUrl").value(this.tomatoDto.getImgUrl()));
        //Verify
        verify(ingredientService).getAllSimpleIngredientsByCategoryId(eq(categoryId), any(Pageable.class));
    }

    @Test
    void getSimpleIngredientById() throws Exception {
        //Arrest
        Integer ingredientId = 1;
        //Mock
        when(ingredientService.getIngredientSimpleResponseDtoById(ingredientId)).thenReturn(this.tomatoDto);
        //Perform Get
        mockMvc.perform(get("/api/ingredients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(this.tomatoDto.getId()))
                .andExpect(jsonPath("$.name").value(this.tomatoDto.getName()))
                .andExpect(jsonPath("$.imgUrl").value(this.tomatoDto.getImgUrl()));
        //Verify
        verify(ingredientService, times(1)).getIngredientSimpleResponseDtoById(ingredientId);
    }

    @Test
    void getIngredientsByName() throws Exception {
        //Arrest
        String ingredientName = "tom";
        Page<IngredientSimpleResponseDto> ingredients = new PageImpl<>(List.of(this.tomatoDto));
        //Mock
        when(ingredientService.getIngredientsSimpleResponseDtoByName(eq(ingredientName), any(Pageable.class)))
                .thenReturn(ingredients);
        //Perform Get
        mockMvc.perform(get("/api/ingredients/by-name/{ingredientName}", ingredientName)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ingredientSimpleResponseDtoList[0].id").value(this.tomatoDto.getId()))
                .andExpect(jsonPath("$._embedded.ingredientSimpleResponseDtoList[0].name").value(this.tomatoDto.getName()))
                .andExpect(jsonPath("$._embedded.ingredientSimpleResponseDtoList[0].imgUrl").value(this.tomatoDto.getImgUrl()));
        //Verify
        verify(ingredientService, times(1)).getIngredientsSimpleResponseDtoByName(eq(ingredientName), any(Pageable.class));
    }

    @Test
    void updateIngredient() throws Exception {
        //Arrest
        String requestBody = """
                    {
                        "name" : "new name",
                        "imgUrl" : "https://example.com/newimg.png"
                    }
                """;

        Integer ingredientId = this.tomatoDto.getId();

        Ingredient updatedIngredient = Ingredient.builder()
                .name("new name")
                .imgUrl("https://example.com/newimg.png")
                .categories(List.of(Category.builder().build()))
                .build();
        //Mock
        when(ingredientService.updateIngredient(eq(ingredientId), any(IngredientUpdateRequestDto.class))).thenReturn(updatedIngredient);
        //Perform Get
        mockMvc.perform(put("/api/ingredients/{ingredientId}", ingredientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        ArgumentCaptor<IngredientUpdateRequestDto> captor = ArgumentCaptor.forClass(IngredientUpdateRequestDto.class);

        //Verify
        verify(ingredientService, times(1)).updateIngredient(eq(ingredientId), captor.capture());

        IngredientUpdateRequestDto ingredientDto = captor.getValue();

        //Assert
        assertNotNull(ingredientDto);
        assertEquals("new name", ingredientDto.getName());
        assertEquals("https://example.com/newimg.png", ingredientDto.getImgUrl());
    }

    @Test
    void updateIngredient_BudUrl() throws Exception {
        //Arrest
        String requestBody = """
                    {
                        "name" : "new name",
                        "imgUrl" : "url"
                    }
                """;

        Integer ingredientId = this.tomatoDto.getId();

        //Perform Get
        mockMvc.perform(put("/api/ingredients/{ingredientId}", ingredientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}