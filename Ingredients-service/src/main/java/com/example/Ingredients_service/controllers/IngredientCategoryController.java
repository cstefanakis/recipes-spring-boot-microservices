package com.example.Ingredients_service.controllers;

import com.example.Ingredients_service.dtos.category.CategoryCreateRequestDto;
import com.example.Ingredients_service.dtos.category.CategoryResponseDto;
import com.example.Ingredients_service.dtos.category.CategoryUpdateRequestDto;
import com.example.Ingredients_service.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient-categories")
@RequiredArgsConstructor
public class IngredientCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@Valid @RequestBody CategoryCreateRequestDto categoryCreateRequestDto){
        categoryService.createCategory(categoryCreateRequestDto);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(){
        List<CategoryResponseDto> categoriesResponseDto = categoryService.getAllCategoryResponseDto();
        return ResponseEntity.ok(categoriesResponseDto);
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
