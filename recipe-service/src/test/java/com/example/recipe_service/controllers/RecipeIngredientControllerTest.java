package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientCreateRequestDto;
import com.example.recipe_service.services.RecipeIngredientService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static com.example.recipe_service.enums.Unit.GRAM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeIngredientController.class)
@ActiveProfiles("test")
class RecipeIngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeIngredientService recipeIngredientService;

    @Test
    void createRecipeIngredient() throws Exception {
        //Arrange
        String requestBody = """
                    {
                        "ingredientId" : 1,
                        "unit" : "GRAM",
                        "quantity" : 20,
                        "recipeId" : 1
                    }
                """;

        //Perform
        mockMvc.perform(post("/api/recipe-ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());

        ArgumentCaptor<RecipeIngredientCreateRequestDto> captor = ArgumentCaptor
                .forClass(RecipeIngredientCreateRequestDto.class);

        //Verify
        verify(recipeIngredientService).createRecipeIngredient(captor.capture());

        RecipeIngredientCreateRequestDto recipeIngredientDto = captor.getValue();

        //Assert
        assertNotNull(recipeIngredientDto);
        assertEquals(1, recipeIngredientDto.getIngredientId());
        assertEquals(GRAM, recipeIngredientDto.getUnit());
        assertEquals(20, recipeIngredientDto.getQuantity());
        assertEquals(1, recipeIngredientDto.getRecipeId());
    }

    @Test
    void createRecipeIngredient_nullIngredientId() throws Exception {
        //Arrange
        String requestBody = """
                    {
                        "unit" : "GRAM",
                        "quantity" : 20,
                        "recipeId" : 1
                    }
                """;

        //Perform
        mockMvc.perform(post("/api/recipe-ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipeIngredient_nullUnit() throws Exception {
        //Arrange
        String requestBody = """
                    {
                        "ingredientId" : 1,
                        "quantity" : 20,
                        "recipeId" : 1
                    }
                """;

        //Perform
        mockMvc.perform(post("/api/recipe-ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipeIngredient_nullQuantity() throws Exception {
        //Arrange
        String requestBody = """
                    {
                        "ingredientId" : 1,
                        "unit" : "GRAM",
                        "recipeId" : 1
                    }
                """;

        //Perform
        mockMvc.perform(post("/api/recipe-ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipeIngredient_nullRecipeId() throws Exception {
        //Arrange
        String requestBody = """
                    {
                        "ingredientId" : 1,
                        "unit" : "GRAM",
                        "quantity" : 20
                    }
                """;

        //Perform
        mockMvc.perform(post("/api/recipe-ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}