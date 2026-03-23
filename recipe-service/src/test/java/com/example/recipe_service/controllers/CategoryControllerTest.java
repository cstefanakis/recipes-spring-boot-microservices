package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.category.CategoryCreateRequestDto;
import com.example.recipe_service.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void createCategory() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "name" : "Sweets",
                    "imgUrl" : "url"
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipe-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());

        ArgumentCaptor<CategoryCreateRequestDto> captor = ArgumentCaptor
                .forClass(CategoryCreateRequestDto.class);
        //Verify
        verify(categoryService, times(1)).createCategory(captor.capture());
        CategoryCreateRequestDto categoryDto = captor.getValue();
        //Assert
        assertNotNull(categoryDto);
        assertEquals("Sweets", categoryDto.getName());
        assertEquals("url", categoryDto.getImgUrl());
    }

    @Test
    void createCategory_nullName() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "imgUrl" : "url"
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipe-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCategory_nullImgUrl() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "name" : "Sweets"
                }
                """;
        //Perform Post
        mockMvc.perform(post("/api/recipe-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        ArgumentCaptor<CategoryCreateRequestDto> captor = ArgumentCaptor
                .forClass(CategoryCreateRequestDto.class);
        //Verify
        verify(categoryService, times(1)).createCategory(captor.capture());
        CategoryCreateRequestDto categoryDto = captor.getValue();
        //Assert
        assertNotNull(categoryDto);
        assertEquals("Sweets", categoryDto.getName());
    }

    @Test
    void getCategories() {
    }

    @Test
    void getCategoryById() {
    }

    @Test
    void updateCategory() {
    }

    @Test
    void deleteCategoryById() {
    }
}