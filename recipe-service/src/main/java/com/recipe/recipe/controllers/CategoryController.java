package com.recipe.recipe.controllers;

import com.recipe.recipe.dtos.category.CategoryCreateRequestDto;
import com.recipe.recipe.dtos.category.CategoryResponseDto;
import com.recipe.recipe.dtos.category.CategoryUpdateRequestDto;
import com.recipe.recipe.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@Valid @RequestBody CategoryCreateRequestDto categoryRequestDto){
        categoryService.createCategory(categoryRequestDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories(){
        List<CategoryResponseDto> categoriesResponseDto = categoryService.getCategories();
        return ResponseEntity.ok(categoriesResponseDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable("categoryId") Integer categoryId){
        CategoryResponseDto categoryResponseDto = categoryService.getCategoryResponseDtoById(categoryId);
        return ResponseEntity.ok(categoryResponseDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCategory(@PathVariable("categoryId") Integer categoryId,
                               @Valid @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto){
        categoryService.updateCategory(categoryId, categoryUpdateRequestDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable("categoryId") Integer categoryId){
        categoryService.deleteCategoryById(categoryId);
    }
}
