package com.example.recipe_step_service.controllers;

import com.example.recipe_step_service.dtos.RecipeStepCreateRequestDto;
import com.example.recipe_step_service.models.RecipeStep;
import com.example.recipe_step_service.services.RecipeStepService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeStepController.class)
@ActiveProfiles("test")
class RecipeStepControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeStepService recipeStepService;

    @Test
    void createRecipeStep() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "stepNumber" : 1,
                    "description" : "description",
                    "imgUrl" : "url",
                    "recipeId" : 1
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipe-steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());

        ArgumentCaptor<RecipeStepCreateRequestDto> captor =
                ArgumentCaptor.forClass(RecipeStepCreateRequestDto.class);

        //Verify
        verify(recipeStepService, times(1))
                .createRecipeStep(captor.capture());

        RecipeStepCreateRequestDto dto = captor.getValue();

        //Assert
        assertEquals(1, dto.getStepNumber());
        assertEquals("description", dto.getDescription());
        assertEquals("url", dto.getImgUrl());
        assertEquals(1, dto.getRecipeId());
    }

    @Test
    void createRecipeStep_descriptionNull() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "stepNumber" : 1,
                    "imgUrl" : "url",
                    "recipeId" : 1
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipe-steps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipeStep_recipeIdNull() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "stepNumber" : 1,
                    "description" : "description",
                    "imgUrl" : "url"
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipe-steps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipeStep_stepNumberNull() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "description" : "description",
                    "imgUrl" : "url",
                    "recipeId" : 1
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipe-steps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipeStep_imgUrlNull() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "stepNumber" : 1,
                    "description" : "description",
                    "recipeId" : 1
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipe-steps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void getRecipeStepsByRecipeId() throws Exception {
        //Perform Get
        mockMvc.perform(get("/api/recipe-steps/recipe/1"))
                .andExpect(status().isOk());
        //Verify
        verify(recipeStepService).getRecipeStepsByRecipeId(1);
    }

    @Test
    void deleteRecipeStepById() throws Exception {
        //Perform Delete
        mockMvc.perform(delete("/api/recipe-steps/1"))
                .andExpect(status().isNoContent());
        //Verify
        verify(recipeStepService).deleteRecipeStepById(1);
    }

    @Test
    void updateRecipeStepById() throws Exception {
        //Mock Service
        String requestBody = """
                {
                   "stepNumber" : 2,
                   "description" : "new description",
                   "imgUrl" : "new url"
                }
                """;
        RecipeStep response = RecipeStep.builder()
                .id(1)
                .stepNumber(2)
                .description("new description")
                .imgUrl("new url")
                .recipeId(1)
                .build();
        //Perform Put
        mockMvc.perform(put("/api/recipe-steps/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }
}