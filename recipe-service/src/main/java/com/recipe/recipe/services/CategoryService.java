package com.recipe.recipe.services;

import com.recipe.recipe.dtos.category.CategoryCreateRequestDto;
import com.recipe.recipe.dtos.category.CategoryResponseDto;
import com.recipe.recipe.dtos.category.CategoryUpdateRequestDto;
import com.recipe.recipe.models.Category;
import com.recipe.recipe.repositories.CategoryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id: %s not found", categoryId)));
    }

    public void createCategory(@Valid CategoryCreateRequestDto categoryRequestDto) {

        Category category = toEntity(categoryRequestDto);
        categoryRepository.save(category);
    }

    private Category toEntity(@Valid CategoryCreateRequestDto categoryRequestDto) {

        String categoryName = validatedName(categoryRequestDto.getName());

        return Category.builder()
                .name(categoryName)
                .imgUrl(categoryRequestDto.getImgUrl())
                .build();
    }

    private String validatedName(@NotBlank String name) {
        boolean nameExists = categoryRepository.existsByName(name);
        if (nameExists){
            throw new EntityExistsException(String.format("Category with name %s already exists", name));
        }
        return name;
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

        Category updatedCategory = toUpdatedEntity(category, categoryUpdateRequestDto);

        categoryRepository.save(updatedCategory);
    }

    private Category toUpdatedEntity(Category category, CategoryUpdateRequestDto categoryUpdateRequestDto) {

        String nameDto = validatedName(categoryUpdateRequestDto.getName());
        String imgUrlDto = categoryUpdateRequestDto.getImgUrl();

        category.setName(updatedName(category.getName(), nameDto));

        category.setImgUrl(imgUrlDto == null
                ? category.getImgUrl()
                : imgUrlDto);

        return category;
    }

    private String updatedName(String name, String nameDto) {

        if(nameDto == null){
            return name;
        }

        if(name.equals(nameDto)){
            return name;
        }

        validatedName(nameDto);

        return nameDto;

    }

    public void deleteCategoryById(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public List<Category> getCategoriesByIds(List<Integer> categoriesId) {
        return categoryRepository.findAllById(categoriesId);
    }
}
