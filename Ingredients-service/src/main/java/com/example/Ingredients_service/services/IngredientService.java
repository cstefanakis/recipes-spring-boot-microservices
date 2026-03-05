package com.example.Ingredients_service.services;

import com.example.Ingredients_service.dtos.IngredientDto;
import com.example.Ingredients_service.dtos.IngredientUpdateDto;
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

    public void createIngredient(IngredientDto ingredientDto){
        Ingredient ingredient = toEntity(ingredientDto);
        ingredientRepository.save(ingredient);
    }

    public Ingredient getIngredientById(Integer ingredientId){
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Ingredient with id: %s not found", ingredientId)));
    }

    public void deleteIngredient(Integer ingredientId){
        ingredientRepository.deleteById(ingredientId);
    }

    public void updateIngredient(Integer ingredientId, IngredientUpdateDto ingredientUpdateDto){
        Ingredient ingredient = getIngredientById(ingredientId);
        updateToEntity(ingredient, ingredientUpdateDto);
    }

    public List<Ingredient> getAllIngredients(){
        return ingredientRepository.findAll();
    }

    private void updateToEntity(Ingredient ingredient, IngredientUpdateDto ingredientUpdateDto) {

        String nameDto = ingredientUpdateDto.getName();
        List<Integer> categoriesIdDto = ingredientUpdateDto.getCategoriesId();

        ingredient.setName(nameDto == null
                ? ingredient.getName()
                : nameDto);
        ingredient.setCategoriesId(categoriesIdDto == null
                ? ingredient.getCategoriesId()
                : categoriesIdDto);
    }

    private Ingredient toEntity(IngredientDto ingredientDto) {
        return Ingredient.builder()
                .name(ingredientDto.getName())
                .categoriesId(ingredientDto.getCategoriesId())
                .build();
    }
}
