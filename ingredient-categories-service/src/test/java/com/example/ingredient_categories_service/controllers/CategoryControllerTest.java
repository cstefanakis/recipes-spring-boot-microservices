package com.example.ingredient_categories_service.controllers;

import com.example.ingredient_categories_service.dtos.CategoryDto;
import com.example.ingredient_categories_service.dtos.FullResponseCategoryDto;
import com.example.ingredient_categories_service.dtos.UpdateCategoryDto;
import com.example.ingredient_categories_service.models.Category;
import com.example.ingredient_categories_service.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        String requestBody = """
                {
                    "name" : "Vegetables"
                }
                """;
        Category response = Category.builder()
                .name("Vegetables")
                .build();
        //Mock Service
        when(categoryService.createCategory(any(CategoryDto.class)))
                .thenReturn(response);
        //Perform Post
        mockMvc.perform(post("/api/ingredient-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").value("Vegetables"));
        //Verify
        verify(categoryService).createCategory(any(CategoryDto.class));
    }

    @Test
    void createCategory_null() throws Exception {
        String requestBody = """
                {
                    "name" : ""
                }
                """;
        Category response = Category.builder()
                .name("Vegetables")
                .build();
        //Mock Service
        when(categoryService.createCategory(any(CategoryDto.class)))
                .thenReturn(response);
        //Perform Post
        mockMvc.perform(post("/api/ingredient-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCategoryById() throws Exception {
        //Mock Service
        when(categoryService.getCategoryById(eq(1))).thenReturn(any(Category.class));

        //Perform Get
        mockMvc.perform(get("/api/ingredient-categories/1"))
                .andExpect(status().isOk());
        //Verify
        verify(categoryService).getCategoryById(1);
    }

    @Test
    void getAllCategories() throws Exception {
        //Perform Get
        mockMvc.perform(get("/api/ingredient-categories"))
                .andExpect(status().isOk());
        //Verify
        verify(categoryService).getAllCategories();
    }

    @Test
    void getCategoryWithIngredientsById() throws Exception {
        //Mock Service
        when(categoryService.getCategoryWithIngredientsById(eq(1))).thenReturn(any(FullResponseCategoryDto.class));
        //Preform Get
        mockMvc.perform(get("/api/ingredient-categories/with-ingredients/1"))
                .andExpect(status().isOk());
        //Verify
        verify(categoryService).getCategoryWithIngredientsById(1);
    }

    @Test
    void updateCategory() throws Exception {
        //Mock Service
        String requestBody = """
                {
                    "name": "Updated Category"
                }
                """;

        Category responseDto = Category.builder()
                .name("Updated Category")
                .build();

        when(categoryService.updateCategory(eq(1), any(UpdateCategoryDto.class)))
                .thenReturn(responseDto);

        //Perform Put
        mockMvc.perform(put("/api/ingredient-categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Category"));
        //Verify
        verify(categoryService).updateCategory(eq(1), any(UpdateCategoryDto.class));
    }

    @Test
    void deleteCategory() throws Exception {
        //Act
        mockMvc.perform(delete("/api/ingredient-categories/1"))
                .andExpect(status().isNoContent());

        //Assert
        verify(categoryService).deleteCategory(1);
    }
}