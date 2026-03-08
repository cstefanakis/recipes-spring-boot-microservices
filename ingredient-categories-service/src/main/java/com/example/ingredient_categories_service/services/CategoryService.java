package com.example.ingredient_categories_service.services;

import com.example.ingredient_categories_service.clients.IngredientClient;
import com.example.ingredient_categories_service.dtos.CategoryDto;
import com.example.ingredient_categories_service.dtos.FullResponseCategoryDto;
import com.example.ingredient_categories_service.dtos.IngredientDto;
import com.example.ingredient_categories_service.dtos.UpdateCategoryDto;
import com.example.ingredient_categories_service.models.Category;
import com.example.ingredient_categories_service.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final IngredientClient ingredientClient;

    public Category createCategory(CategoryDto categoryDto){
        Category category = toEntity(categoryDto);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Integer categoryId){
        categoryRepository.deleteById(categoryId);
    }

    public Category updateCategory(Integer categoryId, UpdateCategoryDto updateCategoryDto){
        Category category = getCategoryById(categoryId);
        return categoryRepository.save(toUpdateEntity(category, updateCategoryDto));
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public FullResponseCategoryDto getCategoryWithIngredientsById(Integer categoryId){
        String categoryName = categoryRepository.findCategoryNameByCategoryId(categoryId);
        List<IngredientDto> ingredients = ingredientClient.getSimpleIngredientByCategoryId(categoryId);
        return FullResponseCategoryDto.builder()
                .name(categoryName)
                .ingredients(ingredients)
                .build();
    }

    private Category toUpdateEntity(Category category, UpdateCategoryDto updateCategoryDto) {
        String name = updateCategoryDto.getName() == null
                ? category.getName()
                : updateCategoryDto.getName();

        category.setName(name);
        return category;
    }

    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Category with id: %s not found", categoryId)));
    }

    private Category toEntity(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }
}
