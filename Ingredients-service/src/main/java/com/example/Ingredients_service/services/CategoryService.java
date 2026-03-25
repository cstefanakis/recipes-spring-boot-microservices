package com.example.Ingredients_service.services;

import com.example.Ingredients_service.dtos.category.CategoryCreateRequestDto;
import com.example.Ingredients_service.dtos.category.CategoryResponseDto;
import com.example.Ingredients_service.dtos.category.CategoryUpdateRequestDto;
import com.example.Ingredients_service.models.Category;
import com.example.Ingredients_service.repositories.CategoryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void createCategory(CategoryCreateRequestDto categoryCreateRequestDto){
        Category category = toEntity(categoryCreateRequestDto);
        categoryRepository.save(category);
    }

    public void updateCategory(Integer categoryId, CategoryUpdateRequestDto updateCategoryDto){
        Category category = getCategoryById(categoryId);
        categoryRepository.save(toUpdateEntity(category, updateCategoryDto));
    }

    public List<CategoryResponseDto> getAllCategoryResponseDto(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::toCategoryResponseDto)
                .toList();
    }

    private Category toUpdateEntity(Category category, CategoryUpdateRequestDto categoryUpdateRequestDto) {

        String nameDto = categoryUpdateRequestDto.getName();

        String name = nameDto == null
                ? category.getName()
                : validatedCategoryName(nameDto);

        category.setName(name);
        return category;
    }

    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Category with id: %s not found", categoryId)));
    }

    private Category toEntity(CategoryCreateRequestDto categoryDto) {
        return Category.builder()
                .name(validatedCategoryName(categoryDto.getName()))
                .imgUrl(categoryDto.getImgUrl())
                .build();
    }

    private String validatedCategoryName(String name) {
        boolean isNameExists = categoryRepository.nameExists(name);
        if(isNameExists){
            throw new EntityExistsException(String.format("Category with name %s already exists", name));
        }
        return name;
    }

    public List<CategoryResponseDto> toCategoriesResponseDto(List<Category> categories) {
        return categories.stream()
                .map(this::toCategoryResponseDto)
                .toList();
    }

    private CategoryResponseDto toCategoryResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .imgUrl(category.getImgUrl())
                .build();
    }

    public void deleteCategoryById(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public CategoryResponseDto getCategoryResponseDtoById(Integer categoryId) {
        Category category = getCategoryById(categoryId);
        return toCategoryResponseDto(category);
    }
}
