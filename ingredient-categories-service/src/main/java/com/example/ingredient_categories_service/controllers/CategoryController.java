package com.example.ingredient_categories_service.controllers;

import com.example.ingredient_categories_service.dtos.CategoryDto;
import com.example.ingredient_categories_service.models.Category;
import com.example.ingredient_categories_service.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingredient-categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto){
        Category category = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(category);
    }
}
