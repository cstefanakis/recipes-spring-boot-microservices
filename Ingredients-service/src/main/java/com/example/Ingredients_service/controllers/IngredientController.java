package com.example.Ingredients_service.controllers;

import com.example.Ingredients_service.dtos.category.CategoryCreateRequestDto;
import com.example.Ingredients_service.dtos.category.CategoryResponseDto;
import com.example.Ingredients_service.dtos.category.CategoryUpdateRequestDto;
import com.example.Ingredients_service.dtos.ingredient.IngredientCreateRequestDto;
import com.example.Ingredients_service.dtos.ingredient.IngredientResponseDto;
import com.example.Ingredients_service.dtos.ingredient.IngredientSimpleResponseDto;
import com.example.Ingredients_service.dtos.ingredient.IngredientUpdateRequestDto;
import com.example.Ingredients_service.models.Ingredient;
import com.example.Ingredients_service.services.CategoryService;
import com.example.Ingredients_service.services.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;
    private final CategoryService categoryService;

    //Ingredients
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createIngredient(@Valid @RequestBody IngredientCreateRequestDto ingredientCreateRequestDto){
        ingredientService.createIngredient(ingredientCreateRequestDto);
    }

    @GetMapping("/with-categories/{ingredientId}")
    public ResponseEntity<IngredientResponseDto> getIngredientWithCategoryById(
            @PathVariable ("ingredientId") Integer ingredientId){

        IngredientResponseDto ingredient = ingredientService.getIngredientWithCategoryById(ingredientId);

        return ResponseEntity.ok(ingredient);
    }

    @GetMapping
    public ResponseEntity<Page<IngredientSimpleResponseDto>> getAllSimpleIngredients(Pageable page){
        Page<IngredientSimpleResponseDto> ingredients = ingredientService.getAllSimpleIngredients(page);
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<Page<IngredientSimpleResponseDto>> getAllIngredientsWithCategories(@PathVariable ("categoryId") Integer categoryId,
                                                                                             Pageable page){
        Page<IngredientSimpleResponseDto> ingredients = ingredientService.getAllSimpleIngredientsByCategoryId(categoryId, page);
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/simple/{ingredientId}")
    public ResponseEntity<IngredientSimpleResponseDto> getSimpleIngredientById(@PathVariable ("ingredientId") Integer ingredientId){
        IngredientSimpleResponseDto IngredientSimpleResponseDto = ingredientService.getIngredientSimpleResponseDtoById(ingredientId);
        return ResponseEntity.ok(IngredientSimpleResponseDto);
    }

    @GetMapping("by-name/{ingredientName}")
    public ResponseEntity<Page<IngredientSimpleResponseDto>> getIngredientsByName(@PathVariable("ingredientName") String ingredientName,
                                                                                  Pageable pageable){
        Page<IngredientSimpleResponseDto> ingredientsSimpleResponseDto = ingredientService.getIngredientsSimpleResponseDtoByName(ingredientName, pageable);
        return ResponseEntity.ok(ingredientsSimpleResponseDto);
    }

    @DeleteMapping("/{ingredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable ("ingredientId") Integer ingredientId){
        ingredientService.deleteIngredient(ingredientId);
    }

    @PutMapping("/{ingredientId}")
    public ResponseEntity<Ingredient> updateIngredient(
            @PathVariable ("ingredientId") Integer ingredientId,
            @RequestBody IngredientUpdateRequestDto ingredientUpdateRequestDto){
        Ingredient updatedIngredient = ingredientService.updateIngredient(ingredientId, ingredientUpdateRequestDto);
        return ResponseEntity.ok(updatedIngredient);
    }

    //Categories

    @PostMapping("/create-category")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@Valid @RequestBody CategoryCreateRequestDto categoryCreateRequestDto){
        categoryService.createCategory(categoryCreateRequestDto);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(){
        List<CategoryResponseDto> categoriesResponseDto = categoryService.getAllCategoryResponseDto();
        return ResponseEntity.ok(categoriesResponseDto);
    }

    @PutMapping("/update-category/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCategory(@PathVariable("categoryId") Integer categoryId,
                               @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto){
        categoryService.updateCategory(categoryId, categoryUpdateRequestDto);
    }

    @DeleteMapping("/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable("categoryId") Integer categoryId){
        categoryService.deleteCategoryById(categoryId);
    }
}
