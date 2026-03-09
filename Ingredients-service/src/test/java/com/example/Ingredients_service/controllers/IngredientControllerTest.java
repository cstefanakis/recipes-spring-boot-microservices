package com.example.Ingredients_service.controllers;

import com.example.Ingredients_service.dtos.*;
import com.example.Ingredients_service.models.Ingredient;
import com.example.Ingredients_service.services.IngredientService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        doNothing().when(ingredientService).createIngredient(any(IngredientDto.class));

        //Perform Post
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
        //Verify
        verify(ingredientService).createIngredient(any(IngredientDto.class));
    }

    @Test
    void createIngredient_nullArgument() throws Exception {
        //Mock Service
        String requestBody = """
            {
                "categories": [1]
            }
            """;

        doNothing().when(ingredientService).createIngredient(any(IngredientDto.class));

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
    void getAllIngredients() throws Exception {
        //Mock Service
        Ingredient tomato = Ingredient.builder()
                .id(1)
                .name("Tomato")
                .categoriesId(List.of(1))
                .build();

        when(ingredientService.getAllIngredients()).thenReturn(List.of(tomato));
        //Perform Get
        mockMvc.perform(get("/api/ingredients"))
                .andExpect(status().isOk());
        //Verify
        verify(ingredientService).getAllIngredients();
    }

    @Test
    void getAllIngredientsWithCategories() throws Exception {
        //Mock Service
        IngredientCategoryDto category = IngredientCategoryDto.builder()
                .name("Vegetables")
                .build();

        IngredientResponseDto ingredientDto = IngredientResponseDto.builder()
                .name("Tomato")
                .categories(List.of(category))
                .build();

        when(ingredientService.getAllIngredientsWithCategories())
                .thenReturn(List.of(ingredientDto));
        //Perform get
        mockMvc.perform(get("/api/ingredients/with-categories"))
                .andExpect(status().isOk());
        //Verify
        verify(ingredientService).getAllIngredientsWithCategories();
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
    void updateIngredient() throws Exception {
        //Mock Service
        String requestBody = """
                {
                    "name" : "Tomato",
                    "categoriesId" : [1]
                }
                """;

        Ingredient responseDto = Ingredient.builder()
                .name("Tomato")
                .categoriesId(List.of(1))
                .build();

        when(ingredientService.updateIngredient(eq(1), any(IngredientUpdateDto.class)))
                .thenReturn(responseDto);
        //Perform Put
        mockMvc.perform(put("/api/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tomato"))
                .andExpect(jsonPath("$.categoriesId[0]").value(1));
        //Verify
        verify(ingredientService).updateIngredient(eq(1), any(IngredientUpdateDto.class));
    }

    @Test
    void getIngredientsSimpleByCategoryId() throws Exception {
        //Mock service

        IngredientSimpleResponseDto ingredient = IngredientSimpleResponseDto.builder()
                .name("Tomato")
                .build();
        List<IngredientSimpleResponseDto> ingredients = List.of(ingredient);
        when(ingredientService.getIngredientsSimpleByCategoryId(eq(1))).thenReturn(ingredients);
        //Perform Get
        mockMvc.perform(get("/api/ingredients/simple/1"))
                .andExpect(status().isOk());
        //Verify
        verify(ingredientService).getIngredientsSimpleByCategoryId(eq(1));
    }
}