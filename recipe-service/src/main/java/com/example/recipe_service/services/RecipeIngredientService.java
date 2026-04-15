package com.example.recipe_service.services;

import com.example.recipe_service.clients.IngredientClient;
import com.example.recipe_service.dtos.ingredient.IngredientSimpleResponseDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientCreateRequestDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientResponseDto;
import com.example.recipe_service.dtos.recipeIngredient.RecipeIngredientUpdateRequestDto;
import com.example.recipe_service.enums.Unit;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.models.RecipeIngredient;
import com.example.recipe_service.repositories.RecipeIngredientRepository;
import com.example.recipe_service.repositories.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeIngredientService {

    private final IngredientClient ingredientClient;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeRepository recipeRepository;
    private final UserService userService;

    public void createRecipeIngredient(RecipeIngredientCreateRequestDto ingredientCreateRequestDto) {

        Integer recipeId = ingredientCreateRequestDto.getRecipeId();

        Integer recipeOwnerId = recipeRepository.findRecipeOwnerIdByRecipeId(recipeId);

        boolean isOwnerOrAdmin = userService.isOwnerOrAdmin(recipeOwnerId);

        if(isOwnerOrAdmin) {

            RecipeIngredient recipeIngredient = toEntity(ingredientCreateRequestDto);

            recipeIngredientRepository.save(recipeIngredient);
        } else {
            throw new RuntimeException("You don't have permission");
        }
    }

    private RecipeIngredient toEntity(RecipeIngredientCreateRequestDto ingredientCreateRequestDto) {

        Integer recipeId = ingredientCreateRequestDto.getRecipeId();

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(()-> new EntityNotFoundException("Recipe with id: " + recipeId + " not found"));

        Integer ingredientId = ingredientClient.ingredientExistById(ingredientCreateRequestDto.getIngredientId());

        return RecipeIngredient.builder()
                .ingredientId(ingredientId)
                .recipe(recipe)
                .unit(ingredientCreateRequestDto.getUnit())
                .quantity(ingredientCreateRequestDto.getQuantity())
                .build();
    }

    public List<RecipeIngredientResponseDto> getRecipeIngredientsByRecipeId(Integer recipeId) {
        List<RecipeIngredient> ingredients = recipeIngredientRepository.findRecipeIngredientsByRecipeId(recipeId);
        return ingredients.stream()
                .map(this::toIngredientResponseDto)
                .toList();
    }

    private RecipeIngredientResponseDto toIngredientResponseDto(RecipeIngredient recipeIngredient) {

        IngredientSimpleResponseDto ingredientSimpleResponseDto = ingredientClient.getIngredientById(recipeIngredient.getId());

        return RecipeIngredientResponseDto.builder()
                .id(recipeIngredient.getId())
                .name(ingredientSimpleResponseDto.getName())
                .imgUrl(ingredientSimpleResponseDto.getImgUrl())
                .unit(recipeIngredient.getUnit())
                .quantity(recipeIngredient.getQuantity())
                .build();
    }

    public void deleteRecipeIngredientById(Integer recipeIngredientId) {

        Integer recipeOwnerId = recipeIngredientRepository.getRecipeOwnerByRecipeIngredientId(recipeIngredientId);

        boolean isOwnerOrAdmin = userService.isOwnerOrAdmin(recipeOwnerId);

        if(isOwnerOrAdmin) {
            recipeIngredientRepository.deleteById(recipeIngredientId);
        } else {
            throw new RuntimeException("you don't have permission");
        }
    }

    public RecipeIngredient getRecipeIngredientById(Integer recipeIngredientId) {

        return recipeIngredientRepository.findById(recipeIngredientId)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Recipe ingredient with id %s not exists", recipeIngredientId)));

    }

    public void updateRecipeIngredient(RecipeIngredient recipeIngredient, RecipeIngredientUpdateRequestDto recipeIngredientUpdateRequestDto) {

        Integer recipeOwnerId = recipeIngredientRepository.getRecipeOwnerByRecipeIngredientId(recipeIngredient.getId());

        boolean isOwnerOrAdmin = userService.isOwnerOrAdmin(recipeOwnerId);

        if(isOwnerOrAdmin) {

            Integer ingredientIdDto = recipeIngredientUpdateRequestDto.getIngredientId();
            Unit unitDto = recipeIngredientUpdateRequestDto.getUnit();
            Double quantityDto = recipeIngredientUpdateRequestDto.getQuantity();

            recipeIngredient.setIngredientId(ingredientIdDto == null
                    ? recipeIngredient.getIngredientId()
                    : ingredientClient.ingredientExistById(ingredientIdDto));

            recipeIngredient.setUnit(unitDto == null
                    ? recipeIngredient.getUnit()
                    : unitDto);

            recipeIngredient.setQuantity(quantityDto == null
                    ? recipeIngredient.getQuantity()
                    : quantityDto);

            recipeIngredientRepository.save(recipeIngredient);

        } else {
            throw new RuntimeException("You don't have permission");
        }
    }

    public List<RecipeIngredient> getAllRecipeIngredients() {
        return recipeIngredientRepository.findAll();
    }

    public RecipeIngredientResponseDto toRecipeIngredientResponseDto(RecipeIngredient recipeIngredient) {

        Integer ingredientId;

        try {
            ingredientId = ingredientClient.ingredientExistById(recipeIngredient.getIngredientId());
        }catch (Exception e){
            ingredientId = null;
        }

        IngredientSimpleResponseDto ingredient = ingredientClient.getIngredientById(ingredientId);

        return RecipeIngredientResponseDto.builder()
                .id(recipeIngredient.getId())
                .name(ingredientId == null
                        ? "ingredient Not Exist"
                        : ingredient.getName())
                .imgUrl(ingredientId == null
                        ? "ingredient Not Exist"
                        : ingredient.getImgUrl())
                .unit(recipeIngredient.getUnit())
                .quantity(recipeIngredient.getQuantity())
                .build();
    }

    public boolean recipeIngredientWithIngredientIdExists(Integer ingredientId) {
        return recipeIngredientRepository.existsWithIngredientId(ingredientId);
    }

    public List<RecipeIngredientResponseDto> getRecipeIngredients(){
        List<RecipeIngredient> recipeIngredients = getAllRecipeIngredients();

        return recipeIngredients.stream()
                .map(this::toRecipeIngredientResponseDto)
                .toList();
    }
}
