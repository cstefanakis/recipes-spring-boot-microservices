package com.example.recipe_service.services;

import com.example.recipe_service.dtos.category.CategoryCreateRequestDto;
import com.example.recipe_service.dtos.category.CategoryResponseDto;
import com.example.recipe_service.dtos.category.CategoryUpdateRequestDto;
import com.example.recipe_service.models.Category;
import com.example.recipe_service.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void createCategory(@Valid CategoryCreateRequestDto categoryRequestDto) {
        Category category = toEntity(categoryRequestDto);
        categoryRepository.save(category);
    }

    private Category toEntity(@Valid CategoryCreateRequestDto categoryRequestDto) {
        return Category.builder()
                .name(categoryRequestDto.getName())
                .imgUrl(categoryRequestDto.getImgUrl())
                .build();
    }

    public CategoryResponseDto toCategoryResponseDto(Category category){
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .imgUrl(category.getImgUrl())
                .build();
    }

    public List<CategoryResponseDto> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::toCategoryResponseDto)
                .toList();
    }

    public CategoryResponseDto getCategoryResponseDtoById(Integer categoryId) {
        Category category = getCategoryById(categoryId);
        return toCategoryResponseDto(category);
    }

    public void updateCategory(Integer categoryId, CategoryUpdateRequestDto categoryUpdateRequestDto) {
        Category category = getCategoryById(categoryId);
        categoryRepository.save(toUpdatedEntity(category, categoryUpdateRequestDto));
    }

    private Category toUpdatedEntity(Category category, CategoryUpdateRequestDto categoryUpdateRequestDto) {
        String nameDto = categoryUpdateRequestDto.getName();
        String imgUrlDto = categoryUpdateRequestDto.getImgUrl();

        category.setName(nameDto == null
                ? category.getName()
                : nameDto);

        category.setImgUrl(imgUrlDto == null
                ? category.getImgUrl()
                : imgUrlDto);

        return category;
    }

    public void deleteCategoryById(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
