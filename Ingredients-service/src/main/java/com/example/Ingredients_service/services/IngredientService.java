package com.example.Ingredients_service.services;

import com.example.Ingredients_service.clients.IngredientCategoryClient;
import com.example.Ingredients_service.dtos.*;
import com.example.Ingredients_service.models.Ingredient;
import com.example.Ingredients_service.repositories.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientCategoryClient ingredientCategoryClient;

    public void createIngredient(IngredientDto ingredientDto){

        //Filter ids if category exists
        List<Integer> filteredCategory = ingredientDto.getCategoriesId().stream()
                .filter(ingredientCategoryClient::ingredientCategoryExistsByCategoryId)
                .toList();

        ingredientDto.setCategoriesId(filteredCategory);

        Ingredient ingredient = toEntity(ingredientDto);
        ingredientRepository.save(ingredient);
    }

    public IngredientResponseDto getIngredientWithCategoryById(Integer ingredientId){
        Ingredient ingredient = getIngredientById(ingredientId);

        List<IngredientCategoryDto> categories = ingredient.getCategoriesId().stream()
                .map(ingredientCategoryClient::getIngredientCategoryById)
                .toList();

        return IngredientResponseDto.builder()
                .name(ingredient.getName())
                .categories(categories)
                .build();
    }

    public void deleteIngredient(Integer ingredientId){
        ingredientRepository.deleteById(ingredientId);
    }

    public Ingredient updateIngredient(Integer ingredientId, IngredientUpdateDto ingredientUpdateDto){
        Ingredient ingredient = getIngredientById(ingredientId);
        return updateToEntity(ingredient, ingredientUpdateDto);
    }

    public List<Ingredient> getAllIngredients(){
        return ingredientRepository.findAll();
    }

    public List<IngredientResponseDto> getAllIngredientsWithCategories(){
        List<Integer> ingredientsId = ingredientRepository.findAllIds();

        return ingredientsId.stream()
                .map(this::getIngredientWithCategoryById)
                .toList();
    }

    public List<IngredientSimpleResponseDto> getIngredientsSimpleByCategoryId(Integer categoryId){

        List<Integer> ingredientsId = ingredientRepository.findAllIdByCategoryId(categoryId);

        return ingredientsId.stream()
                .map(ingredientId -> IngredientSimpleResponseDto.builder()
                        .name(getIngredientNameById(ingredientId))
                        .build()).toList();
    }

    private String getIngredientNameById(Integer ingredientId) {
        return ingredientRepository.findIngredientNameById(ingredientId).orElseThrow(() -> new EntityNotFoundException(
                String.format("Ingredient with id: %s not found", ingredientId)
        ));
    }

    private Ingredient getIngredientById(Integer ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Ingredient with id: %s not found", ingredientId)));
    }

    private Ingredient updateToEntity(Ingredient ingredient, IngredientUpdateDto ingredientUpdateDto) {

        String nameDto = ingredientUpdateDto.getName();
        List<Integer> categoriesIdDto = ingredientUpdateDto.getCategoriesId();

        ingredient.setName(nameDto == null
                ? ingredient.getName()
                : nameDto);
        ingredient.setCategoriesId(categoriesIdDto == null
                ? ingredient.getCategoriesId()
                : categoriesIdDto);

        return ingredientRepository.save(ingredient);
    }

    private Ingredient toEntity(IngredientDto ingredientDto) {
        return Ingredient.builder()
                .name(ingredientDto.getName())
                .categoriesId(ingredientDto.getCategoriesId())
                .build();
    }
}
