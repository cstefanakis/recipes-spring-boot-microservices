package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.category.CategoryCreateRequestDto;
import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.dtos.category.CategoryUpdateRequestDto;
import com.example.recipe_service.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@Valid @RequestBody CategoryCreateRequestDto categoryRequestDto){
        categoryService.createCategory(categoryRequestDto);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories(){
        List<CategoryResponseDto> categoriesResponseDto = categoryService.getCategories();
        return ResponseEntity.ok(categoriesResponseDto);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable("categoryId") Integer categoryId){
        CategoryResponseDto categoryResponseDto = categoryService.getCategoryResponseDtoById(categoryId);
        return ResponseEntity.ok(categoryResponseDto);
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCategory(@PathVariable("categoryId") Integer categoryId,
                               @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto){
        categoryService.updateCategory(categoryId, categoryUpdateRequestDto);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable("categoryId") Integer categoryId){
        categoryService.deleteCategoryById(categoryId);
    }
}
