package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.category.CategoryCreateRequestDto;
import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.dtos.category.CategoryUpdateRequestDto;
import com.example.recipe_service.jwt.JwtFilter;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.repositories.CategoryRepository;
import com.example.recipe_service.services.CategoryService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private CategoryRepository categoryRepository;

    @MockitoBean
    private JwtFilter jwtFilter;

    private Category category;
    private CategoryResponseDto categoryResponseDto;

    @BeforeEach
    void setup(){
        this.category = Category.builder()
                .id(1)
                .name("Category")
                .imgUrl("url")
                .build();

        this.categoryResponseDto = CategoryResponseDto.builder()
                .id(this.category.getId())
                .name(this.category.getName())
                .imgUrl(this.category.getImgUrl())
                .build();
    }

    @Test
    void createCategory() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "name" : "Sweets",
                    "imgUrl" : "http://img.png"
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
        assertEquals("http://img.png", categoryDto.getImgUrl());
    }

    @Test
    void createCategory_BadUrl() throws Exception {
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
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCategory_nullName() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "imgUrl" : "http://img.png"
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
    void getCategories() throws Exception {
        //Arrange
        List<CategoryResponseDto> categories = List.of(this.categoryResponseDto);
        //Mock
        when(categoryService.getCategories()).thenReturn(categories);
        //Preform Get
        mockMvc.perform(get("/api/recipe-categories"))
                .andExpect(status().isOk());
        //Verify
        verify(categoryService).getCategories();
    }

    @Test
    void getCategoryById() throws Exception {
        //Arrange
        Integer categoryId = this.category.getId();
        //Mock
        when(categoryService.getCategoryResponseDtoById(categoryId))
                .thenReturn(this.categoryResponseDto);
        //Preform get
        mockMvc.perform(get("/api/recipe-categories/{categoryId}", categoryId))
                .andExpect(status().isOk());
        //Verify
        verify(categoryService).getCategoryResponseDtoById(categoryId);
    }

    @Test
    void updateCategory() throws Exception {
        //Arrange
        String requestBody = """
                {
                    "name" : "Sweets",
                    "imgUrl" : "http://img.png"
                }
                """;
        Integer categoryId = this.category.getId();
        //Mock
        doNothing().when(categoryService).updateCategory(eq(categoryId), any(CategoryUpdateRequestDto.class));
        //Perform put
        mockMvc.perform(put("/api/recipe-categories/{categoryId}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        ArgumentCaptor<CategoryUpdateRequestDto> captor = ArgumentCaptor
                .forClass(CategoryUpdateRequestDto.class);
        //Verify
        verify(categoryService, times(1))
                .updateCategory(eq(categoryId), captor.capture());

        CategoryUpdateRequestDto categoryDto = captor.getValue();
        //Assert
        assertNotNull(categoryDto);
        assertEquals("Sweets", categoryDto.getName());
        assertEquals("http://img.png", categoryDto.getImgUrl());
    }

    @Test
    void deleteCategoryById() throws Exception {
        //Arrange
        Integer categoryId = this.category.getId();
        //Mock
        doNothing().when(categoryService).deleteCategoryById(categoryId);
        //Perform delete
        mockMvc.perform(delete("/api/recipe-categories/{categoryId}", categoryId))
                .andExpect(status().isNoContent());
        //Verify
        verify(categoryService).deleteCategoryById(categoryId);
    }
}