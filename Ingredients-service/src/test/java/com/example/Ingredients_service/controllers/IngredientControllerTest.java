package com.example.Ingredients_service.controllers;

import com.example.Ingredients_service.dtos.ingredient.*;
import com.example.Ingredients_service.models.Category;
import com.example.Ingredients_service.models.Ingredient;
import com.example.Ingredients_service.services.IngredientService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IngredientController.class)
@ActiveProfiles("test")
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientService ingredientService;

    private IngredientSimpleResponseDto tomatoDto;

    @BeforeEach
    void setup(){
        this.tomatoDto = IngredientSimpleResponseDto.builder()
                .id(1)
                .name("Tomato")
                .imgUrl("url")
                .build();
    }

    @Test
    void createIngredient() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "name" : "Tomato",
                    "imgUrl" : "url",
                    "categories" : [1]
                }
                """;
        //Mock
        doNothing().when(ingredientService).createIngredient(any(IngredientCreateRequestDto.class));

        //Perform Post
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
        //Verify
        verify(ingredientService).createIngredient(any(IngredientCreateRequestDto.class));
    }

    @Test
    void createIngredient_nullArgument() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "categories": [1]
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
        //Mock Service
        when(ingredientService.getIngredientWithCategoryById(eq(1))).thenReturn(any(IngredientResponseDto.class));
        //Perform get
        mockMvc.perform(get("/api/ingredients/with-categories/1"))
                .andExpect(status().isOk());
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
                .andExpect(status().isOk());
        //Verify
        verify(ingredientService, times(1)).getAllSimpleIngredients(any(Pageable.class));
    }

    @Test
    void getAllIngredientsWithCategories() throws Exception {
        //Arrest
        Integer categoryId = 1;
        Page<IngredientSimpleResponseDto> ingredients = new PageImpl<>(List.of(this.tomatoDto));
        //Mock
        when(ingredientService.getAllSimpleIngredientsByCategoryId(eq(categoryId), any(Pageable.class))).thenReturn(ingredients);
        //Perform Get
        mockMvc.perform(get("/api/ingredients/by-category/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
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
        mockMvc.perform(get("/api/ingredients/simple/1"))
                .andExpect(status().isOk());
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
                .andExpect(status().isOk());
        //Verify
        verify(ingredientService, times(1)).getIngredientsSimpleResponseDtoByName(eq(ingredientName), any(Pageable.class));
    }

    @Test
    void updateIngredient() throws Exception {
        //Arrest
        String requestBody = """
                    {
                        "name" : "new name",
                        "imgUrl" : "new url"
                    }
                """;

        Integer ingredientId = this.tomatoDto.getId();

        Ingredient updatedIngredient = Ingredient.builder()
                .name("new name")
                .imgUrl("new url")
                .categories(List.of(Category.builder().build()))
                .build();
        //Mock
        when(ingredientService.updateIngredient(eq(ingredientId), any(IngredientUpdateRequestDto.class))).thenReturn(updatedIngredient);
        //Perform Get
        mockMvc.perform(put("/api/ingredients/{ingredientId}", ingredientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        //Verify
        verify(ingredientService, times(1)).updateIngredient(eq(ingredientId), any(IngredientUpdateRequestDto.class));
    }
}