package com.example.Ingredients_service.services;

import com.example.Ingredients_service.dtos.category.CategoryResponseDto;
import com.example.Ingredients_service.dtos.ingredient.*;
import com.example.Ingredients_service.models.Category;
import com.example.Ingredients_service.models.Ingredient;
import com.example.Ingredients_service.repositories.IngredientRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final CategoryService categoryService;

    public void createIngredient(IngredientCreateRequestDto ingredientCreateRequestDto){
        Ingredient ingredient = toEntity(ingredientCreateRequestDto);
        ingredientRepository.save(ingredient);
    }

    public IngredientResponseDto getIngredientWithCategoryById(Integer ingredientId){
        Ingredient ingredient = getIngredientById(ingredientId);

        List<Category> categories = ingredient.getCategories();

        List<CategoryResponseDto> categoryResponseDto = categoryService.toCategoriesResponseDto(categories);

        return IngredientResponseDto.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .categories(categoryResponseDto)
                .imgUrl(ingredient.getImgUrl())
                .build();
    }

    public void deleteIngredient(Integer ingredientId){
        ingredientRepository.deleteById(ingredientId);
    }

    public Ingredient updateIngredient(Integer ingredientId, IngredientUpdateRequestDto ingredientUpdateRequestDto){
        Ingredient ingredient = getIngredientById(ingredientId);
        return updateToEntity(ingredient, ingredientUpdateRequestDto);
    }

    private Ingredient getIngredientById(Integer ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Ingredient with id: %s not found", ingredientId)));
    }

    private Ingredient updateToEntity(Ingredient ingredient, IngredientUpdateRequestDto ingredientUpdateRequestDto) {

        String nameDto = ingredientUpdateRequestDto.getName();

        List<Integer> ingredientsId = ingredientUpdateRequestDto.getCategoriesId();

        List<Category> categories = ingredientsId.stream()
                .map(categoryService::getCategoryById)
                .toList();

        ingredient.setName(nameDto == null
                ? ingredient.getName()
                : validatedIngredientName(nameDto));
        ingredient.setCategories(ingredientsId.isEmpty()
                ? ingredient.getCategories()
                : categories);

        return ingredientRepository.save(ingredient);
    }

    private Ingredient toEntity(IngredientCreateRequestDto ingredientCreateRequestDto) {

        List<Integer> categoriesId = ingredientCreateRequestDto.getCategoriesId();

        List<Category> categories = categoriesId.stream()
                .map(categoryService::getCategoryById)
                .toList();

        return Ingredient.builder()
                .name(validatedIngredientName(ingredientCreateRequestDto.getName()))
                .imgUrl(ingredientCreateRequestDto.getImgUrl())
                .categories(categories)
                .build();
    }

    private String validatedIngredientName(String name) {
        boolean isNameExists = ingredientRepository.nameExists(name);
        if(isNameExists) {
            throw new EntityExistsException(String.format("Ingredient with name %s already exists", name));
        }
        return name;
    }

    public IngredientSimpleResponseDto getIngredientSimpleResponseDtoById(Integer ingredientId) {

        Ingredient ingredient = getIngredientById(ingredientId);

        return toIngredientSimpleResponseDto(ingredient);
    }

    private IngredientSimpleResponseDto toIngredientSimpleResponseDto(Ingredient ingredient) {
        return IngredientSimpleResponseDto.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .imgUrl(ingredient.getImgUrl())
                .build();
    }

    public Page<IngredientSimpleResponseDto> getAllSimpleIngredients(Pageable pageable) {
        Page<Ingredient> ingredients = ingredientRepository.findAll(pageable);
        return ingredients.map(this::toIngredientSimpleResponseDto);
    }

    public Page<IngredientSimpleResponseDto> getAllSimpleIngredientsByCategoryId(Integer categoryId, Pageable pageable) {
        Page<Ingredient> ingredients = ingredientRepository.findAllByCategoryId(categoryId, pageable);
        return ingredients.map(this::toIngredientSimpleResponseDto);
    }

    public Page<IngredientSimpleResponseDto> getIngredientsSimpleResponseDtoByName(String ingredientName,
                                                                                   Pageable pageable) {
        Page<Ingredient> ingredients = ingredientRepository.findAllByName(ingredientName,
                pageable);
        return ingredients.map(this::toIngredientSimpleResponseDto);
    }

    public Integer ingredientId(Integer ingredientId) {
        Integer id = ingredientRepository.ingredientId(ingredientId);
        if(id == null){
            throw new EntityNotFoundException(String.format("Ingredient with id %s not found"));
        }
        return id;
    }
}
