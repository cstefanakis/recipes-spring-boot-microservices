package com.example.Ingredients_service.controllers;

import com.example.Ingredients_service.dtos.ingredient.*;
import com.example.Ingredients_service.services.IngredientService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


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

    @Test
    void createIngredient() throws Exception {
        //Mock Service
        String requestBody = """
                {
                    "name": "Tomato",
                    "categories": [1]
                }
                """;
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
        //Mock Service
        String requestBody = """
                {
                    "categories": [1]
                }
                """;

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
    void getAllSimpleIngredients() {
    }

    @Test
    void getAllIngredientsWithCategories() {
    }

    @Test
    void getSimpleIngredientById() {
    }

    @Test
    void getIngredientsByName() {
    }

    @Test
    void testDeleteIngredient() {
    }

    @Test
    void updateIngredient() {
    }

    @Test
    void createCategory() {
    }

    @Test
    void getAllCategories() {
    }

    @Test
    void updateCategory() {
    }

    @Test
    void deleteCategoryById() {
    }
}