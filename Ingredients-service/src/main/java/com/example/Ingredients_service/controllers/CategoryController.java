package com.example.Ingredients_service.controllers;

import com.example.Ingredients_service.dtos.category.CategoryCreateRequestDto;
import com.example.Ingredients_service.dtos.category.CategoryResponseDto;
import com.example.Ingredients_service.dtos.category.CategoryUpdateRequestDto;
import com.example.Ingredients_service.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient-categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryCreateRequestDto categoryCreateRequestDto){

        categoryService.createCategory(categoryCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(){

        List<CategoryResponseDto> categoriesResponseDto =
                categoryService.getAllCategoryResponseDto();

        return ResponseEntity.ok(categoriesResponseDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable("categoryId") Integer categoryId){

        CategoryResponseDto categoryResponseDto =
                categoryService.getCategoryResponseDtoById(categoryId);

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