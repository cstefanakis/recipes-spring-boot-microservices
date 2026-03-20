package com.example.Ingredients_service.controllers;

import com.example.Ingredients_service.dtos.category.CategoryResponseDto;
import com.example.Ingredients_service.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IngredientCategoryController.class)
@ActiveProfiles("test")
class IngredientCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void createCategory() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "name" : "Vegetables",
                    "imgUrl" : "url"
                }
                """;
        //Perform
        mockMvc.perform(post("/api/ingredient-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void createCategory_nullName() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "imgUrl" : "url"
                }
                """;
        //Perform
        mockMvc.perform(post("/api/ingredient-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCategory_nullImgUrl() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "name" : "Vegetables"
                }
                """;
        //Perform
        mockMvc.perform(post("/api/ingredient-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllCategories() throws Exception {
        //Arrest
        CategoryResponseDto vegetables = CategoryResponseDto.builder()
                .name("Vegetables")
                .imgUrl("url")
                .build();

        List<CategoryResponseDto> categories = List.of(vegetables);
        //Mock
        when(categoryService.getAllCategoryResponseDto()).thenReturn(categories);
        //Perform
        mockMvc.perform(get("/api/ingredient-categories"))
                .andExpect(status().isOk());
        //Verify
        verify(categoryService, times(1)).getAllCategoryResponseDto();
    }

    @Test
    void updateCategory() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "name" : "Vegetables",
                    "imgUrl" : "url"
                }
                """;

        Integer categoryId = 1;
        //Perform
        mockMvc.perform(put("/api/ingredient-categories/{categoryId}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCategoryById() throws Exception {
        //Arrest
        Integer categoryId = 1;
        //Perform
        mockMvc.perform(delete("/api/ingredient-categories/{categoryId}", categoryId))
                .andExpect(status().isNoContent());
    }
}