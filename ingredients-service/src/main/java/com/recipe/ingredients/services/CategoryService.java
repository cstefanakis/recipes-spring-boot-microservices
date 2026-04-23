package com.recipe.ingredients.services;

import com.recipe.ingredients.dtos.category.CategoryCreateRequestDto;
import com.recipe.ingredients.dtos.category.CategoryResponseDto;
import com.recipe.ingredients.dtos.category.CategoryUpdateRequestDto;
import com.recipe.ingredients.models.Category;
import com.recipe.ingredients.repositories.CategoryRepository;
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

        Category updatedCategory = toUpdateEntity(category, updateCategoryDto);

        categoryRepository.save(updatedCategory);
    }

    public List<CategoryResponseDto> getAllCategoryResponseDto(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::toCategoryResponseDto)
                .toList();
    }

    private Category toUpdateEntity(Category category, CategoryUpdateRequestDto categoryUpdateRequestDto) {

        String nameDto = categoryUpdateRequestDto.getName();

        String name = category.getName();

        String updatedName = updatedName(name, nameDto);

        category.setName(updatedName);

        return category;
    }

    private String updatedName(String name, String nameDto) {

        if(nameDto == null){
            return name;
        }

        if(name.equals(nameDto)){
            return name;
        }

        validatedCategoryName(nameDto);

        return nameDto;
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
