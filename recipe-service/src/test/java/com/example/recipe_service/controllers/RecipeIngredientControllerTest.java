package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientCreateRequestDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientUpdateRequestDto;
import com.example.recipe_service.enums.Unit;
import com.example.recipe_service.models.RecipeIngredient;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Test
    void deleteRecipeIngredientById() throws Exception {
        //Arrange
        Integer recipeIngredientId = 1;
        //Mock
        doNothing().when(recipeIngredientService).deleteRecipeIngredientById(recipeIngredientId);
        //Perform delete
        mockMvc.perform(delete("/api/recipe-ingredients/{recipeIngredientId}", recipeIngredientId))
                .andExpect(status().isNoContent());
        //Verify
        verify(recipeIngredientService, times(1)).deleteRecipeIngredientById(recipeIngredientId);
    }

    @Test
    void updateRecipeIngredientById() throws Exception {
        //Arrange
        Integer recipeIngredientId = 1;
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .id(1)
                .ingredientId(2)
                .unit(Unit.PCS)
                .quantity(20.0)
                .build();
        String requestBody = """
                {
                    "ingredientId" : 1,
                    "unit" : "GRAM",
                    "quantity" : "10.0"
                }
                """;
        //Mock
        when(recipeIngredientService.getRecipeIngredientById(recipeIngredientId)).thenReturn(recipeIngredient);
        doNothing().when(recipeIngredientService).updateRecipeIngredient(any(RecipeIngredient.class), any(RecipeIngredientUpdateRequestDto.class));
        //Perform Put
        mockMvc.perform(put("/api/recipe-ingredients/{recipeIngredientId}", recipeIngredientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        ArgumentCaptor<RecipeIngredientUpdateRequestDto> captor = ArgumentCaptor
                .forClass(RecipeIngredientUpdateRequestDto.class);

        //Verify
        verify(recipeIngredientService).getRecipeIngredientById(recipeIngredientId);
        verify(recipeIngredientService, times(1)).updateRecipeIngredient(any(RecipeIngredient.class), captor.capture());

        RecipeIngredientUpdateRequestDto recipeIngredientDto = captor.getValue();

        //Assert
        assertNotNull(recipeIngredientDto);
        assertEquals(1, recipeIngredientDto.getIngredientId());
        assertEquals("GRAM", recipeIngredientDto.getUnit().toString());
        assertEquals(10.0, recipeIngredientDto.getQuantity());
    }

    @Test
    void ingredientIdExists() throws Exception {
        //Arrange
        Integer ingredientId = 1;
        //Mock
        when(recipeIngredientService.recipeIngredientWithIngredientIdExists(ingredientId))
                .thenReturn(true);
        //Perform Get
        mockMvc.perform(get("/api/recipe-ingredients/ingredient-id-exists/{ingredientId}", ingredientId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        //Verify
        verify(recipeIngredientService).recipeIngredientWithIngredientIdExists(ingredientId);
    }
}