package com.example.recipe_service.services;

import com.example.recipe_service.dtos.category.CategoryRequestDto;
import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public boolean existsCategoryById(Integer categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id: %s not found", categoryId)));
    }

    public void createCategory(@Valid CategoryRequestDto categoryRequestDto) {
        Category category = toEntity(categoryRequestDto);
        categoryRepository.save(category);
    }

    private Category toEntity(@Valid CategoryRequestDto categoryRequestDto) {
        return Category.builder()
                .name(categoryRequestDto.getName())
                .imgUrl(categoryRequestDto.getImgUrl())
                .build();
    }

    public CategoryResponseDto toCategoryResponseDto(Category category){
        return CategoryResponseDto.builder()
                .name(category.getName())
                .imgUrl(category.getImgUrl())
                .build();
    }
}
