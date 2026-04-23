package com.recipe.ingredients.controllers;

import com.recipe.ingredients.dtos.category.CategoryCreateRequestDto;
import com.recipe.ingredients.dtos.category.CategoryResponseDto;
import com.recipe.ingredients.dtos.category.CategoryUpdateRequestDto;
import com.recipe.ingredients.jwt.JwtFilter;
import com.recipe.ingredients.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private JwtFilter jwtFilter;

    private CategoryResponseDto vegetables;

    @BeforeEach
    void setup(){
        this.vegetables = CategoryResponseDto.builder()
                .id(1)
                .name("Vegetables")
                .imgUrl("https://example.com/img.png")
                .build();
    }

    @Test
    void createCategory() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "name" : "Vegetables",
                    "imgUrl" : "https://example.com/img.png"
                }
                """;

        //Perform
        mockMvc.perform(post("/api/ingredient-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());

        ArgumentCaptor<CategoryCreateRequestDto> captor = ArgumentCaptor.forClass(CategoryCreateRequestDto.class);

        //Verify
        verify(categoryService, times(1)).createCategory(captor.capture());

        CategoryCreateRequestDto categoryDto = captor.getValue();

        //Assert
        assertNotNull(categoryDto);
        assertEquals("Vegetables", categoryDto.getName());
        assertEquals("https://example.com/img.png", categoryDto.getImgUrl());
    }

    @Test
    void createCategory_nullName() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "imgUrl" : "https://example.com/img.png"
                }
                """;
        //Perform
        mockMvc.perform(post("/api/ingredient-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCategory_badUrl() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "name" : "Vegetables",
                    "imgUrl" : "ulr"
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
        List<CategoryResponseDto> categories = List.of(this.vegetables);
        //Mock
        when(categoryService.getAllCategoryResponseDto()).thenReturn(categories);
        //Perform
        mockMvc.perform(get("/api/ingredient-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(this.vegetables.getId()))
                .andExpect(jsonPath("$[0].name").value(this.vegetables.getName()))
                .andExpect(jsonPath("$[0].imgUrl").value(this.vegetables.getImgUrl()));
        //Verify
        verify(categoryService, times(1)).getAllCategoryResponseDto();
    }

    @Test
    void updateCategory() throws Exception {
        //Arrest
        String requestBody = """
                {
                    "name" : "Vegetables",
                    "imgUrl" : "https://example.com/img.png"
                }
                """;

        Integer categoryId = 1;

        //Perform
        mockMvc.perform(put("/api/ingredient-categories/{categoryId}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        ArgumentCaptor<CategoryUpdateRequestDto> captor = ArgumentCaptor.forClass(CategoryUpdateRequestDto.class);

        //Verify
        verify(categoryService).updateCategory(eq(categoryId), captor.capture());

        CategoryUpdateRequestDto categoryDto = captor.getValue();

        //Assert
        assertNotNull(categoryDto);
        assertEquals("Vegetables", categoryDto.getName());
        assertEquals("https://example.com/img.png", categoryDto.getImgUrl());
    }

    @Test
    void deleteCategoryById() throws Exception {
        //Arrest
        Integer categoryId = 1;
        //Perform
        mockMvc.perform(delete("/api/ingredient-categories/{categoryId}", categoryId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getCategoryById() throws Exception {
        //Arrest
        Integer categoryId = 1;
        //Mock
        when(categoryService.getCategoryResponseDtoById(categoryId)).thenReturn(this.vegetables);
        //Perform Get
        mockMvc.perform(get("/api/ingredient-categories/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Vegetables"))
                .andExpect(jsonPath("$.imgUrl").value("https://example.com/img.png"));
    }
}