package com.example.Ingredients_service.controllers;

import com.example.Ingredients_service.dtos.ingredient.IngredientCreateRequestDto;
import com.example.Ingredients_service.dtos.ingredient.IngredientResponseDto;
import com.example.Ingredients_service.dtos.ingredient.IngredientSimpleResponseDto;
import com.example.Ingredients_service.dtos.ingredient.IngredientUpdateRequestDto;
import com.example.Ingredients_service.models.Ingredient;
import com.example.Ingredients_service.services.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createIngredient(@Valid @RequestBody IngredientCreateRequestDto ingredientCreateRequestDto){

        ingredientService.createIngredient(ingredientCreateRequestDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/with-categories/{ingredientId}")
    public ResponseEntity<IngredientResponseDto> getIngredientWithCategoryById(
            @PathVariable ("ingredientId") Integer ingredientId){

        IngredientResponseDto ingredient =
                ingredientService.getIngredientWithCategoryById(ingredientId);

        return ResponseEntity.ok(ingredient);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<IngredientSimpleResponseDto>>> getAllSimpleIngredients(Pageable page,
                                                                                           PagedResourcesAssembler<IngredientSimpleResponseDto> assembler){
        Page<IngredientSimpleResponseDto> ingredients =
                ingredientService.getAllSimpleIngredients(page);

        return ResponseEntity.ok(assembler.toModel(ingredients));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<PagedModel<EntityModel<IngredientSimpleResponseDto>>> getAllSimpleIngredientsByCategoryId(@PathVariable ("categoryId") Integer categoryId,
                                                                                                                    Pageable page,
                                                                                                                    PagedResourcesAssembler<IngredientSimpleResponseDto> assembler){
        Page<IngredientSimpleResponseDto> ingredients =
                ingredientService.getAllSimpleIngredientsByCategoryId(categoryId, page);

        return ResponseEntity.ok(assembler.toModel(ingredients));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{ingredientId}")
    public ResponseEntity<IngredientSimpleResponseDto> getSimpleIngredientById(@PathVariable ("ingredientId") Integer ingredientId){

        IngredientSimpleResponseDto IngredientSimpleResponseDto =
                ingredientService.getIngredientSimpleResponseDtoById(ingredientId);

        return ResponseEntity.ok(IngredientSimpleResponseDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("by-name/{ingredientName}")
    public ResponseEntity<PagedModel<EntityModel<IngredientSimpleResponseDto>>> getIngredientsByName(@PathVariable("ingredientName") String ingredientName,
                                                                                                     Pageable pageable,
                                                                                                     PagedResourcesAssembler<IngredientSimpleResponseDto> assembler){
        Page<IngredientSimpleResponseDto> ingredientsSimpleResponseDto =
                ingredientService.getIngredientsSimpleResponseDtoByName(ingredientName, pageable);

        return ResponseEntity.ok(assembler.toModel(ingredientsSimpleResponseDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{ingredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable ("ingredientId") Integer ingredientId){

        ingredientService.deleteIngredient(ingredientId);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{ingredientId}")
    public ResponseEntity<Ingredient> updateIngredient( @PathVariable ("ingredientId") Integer ingredientId,
                                                        @Valid @RequestBody IngredientUpdateRequestDto ingredientUpdateRequestDto){
        Ingredient updatedIngredient =
                ingredientService.updateIngredient(ingredientId, ingredientUpdateRequestDto);

        return ResponseEntity.ok(updatedIngredient);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/exists/{ingredientId}")
    public ResponseEntity<Integer> ingredientExistById(@PathVariable("ingredientId") Integer ingredientId){

        Integer ingredientExists =
                ingredientService.ingredientId(ingredientId);

        return ResponseEntity.ok(ingredientExists);
    }
}
