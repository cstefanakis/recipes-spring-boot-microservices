package com.example.ingredient_categories_service.controllers;

import com.example.ingredient_categories_service.dtos.CategoryDto;
import com.example.ingredient_categories_service.dtos.FullResponseCategoryDto;
import com.example.ingredient_categories_service.dtos.UpdateCategoryDto;
import com.example.ingredient_categories_service.models.Category;
import com.example.ingredient_categories_service.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ingredient-categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        Category createdCategory = categoryService.createCategory(categoryDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{categoryId}")
                .buildAndExpand(createdCategory.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdCategory);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable ("categoryId") Integer categoryId){
        Category category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/with-ingredients/{categoryId}")
    public ResponseEntity<FullResponseCategoryDto> getCategoryWithIngredientsById(@PathVariable ("categoryId") Integer categoryId){
        FullResponseCategoryDto categoryWithIngredients = categoryService.getCategoryWithIngredientsById(categoryId);
        return ResponseEntity.ok(categoryWithIngredients);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable ("categoryId") Integer categoryId,
                                                   @RequestBody UpdateCategoryDto updateCategoryDto){
        Category updatedCategory = categoryService.updateCategory(categoryId, updateCategoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable ("categoryId") Integer categoryId){
        categoryService.deleteCategory(categoryId);
    }
}
