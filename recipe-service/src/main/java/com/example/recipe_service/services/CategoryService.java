package com.example.recipe_service.services;

import com.example.recipe_service.models.Category;
import com.example.recipe_service.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
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
}
