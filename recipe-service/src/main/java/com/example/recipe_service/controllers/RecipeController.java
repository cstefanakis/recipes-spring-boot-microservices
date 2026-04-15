package com.example.recipe_service.controllers;

import com.example.recipe_service.dtos.recipe.RecipeCreateRequestDto;
import com.example.recipe_service.dtos.recipe.RecipeSimpleResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeResponseDto;
import com.example.recipe_service.dtos.recipe.RecipeUpdateRequestDto;
import com.example.recipe_service.models.Recipe;
import com.example.recipe_service.services.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecipe(@Valid @RequestBody RecipeCreateRequestDto recipeRequestDto){
        recipeService.createRecipe(recipeRequestDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponseDto> getRecipeById(@PathVariable ("recipeId") Integer recipeId){
        Recipe recipe = recipeService.getRecipeById(recipeId);
        RecipeResponseDto recipeDto = recipeService.toRecipeResponseDto(recipe);
        return ResponseEntity.ok(recipeDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/owner-id/{recipeId}")
    public ResponseEntity<Integer> getRecipeOwnerId(@PathVariable("recipeId") Integer recipeId){
        Integer ownerId = recipeService.getRecipeOwnerIdByRecipeId(recipeId);
        return ResponseEntity.ok(ownerId);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<RecipeSimpleResponseDto>>> getAllRecipes(Pageable pageable,
                                                                                          PagedResourcesAssembler<RecipeSimpleResponseDto> assembler){

        Page<RecipeSimpleResponseDto> categories
                = recipeService.getAllSimpleRecipes(pageable);

        return ResponseEntity.ok(assembler.toModel(categories));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<PagedModel<EntityModel<RecipeSimpleResponseDto>>> getRecipesByCategoryId(@PathVariable("categoryId") Integer categoryId,
                                                                                                   Pageable pageable,
                                                                                                   PagedResourcesAssembler<RecipeSimpleResponseDto> assembler){

        Page<RecipeSimpleResponseDto> recipesSimpleResponseDto =
                recipeService.getRecipesByCategoryId(categoryId, pageable);

        return ResponseEntity.ok(assembler.toModel(recipesSimpleResponseDto));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{recipeId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRecipeById(@PathVariable("recipeId") Integer recipeId,
                                 @RequestBody RecipeUpdateRequestDto recipeUpdateRequestDto){

        Recipe recipe =
                recipeService.getRecipeById(recipeId);

        recipeService.updateRecipe(recipe, recipeUpdateRequestDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipeById(@PathVariable("recipeId") Integer recipeId){

        recipeService.deleteRecipeById(recipeId);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/exists/{recipeId}")
    public ResponseEntity<Boolean> recipeExists(@PathVariable("recipeId") Integer recipeId){

        boolean exist =
                recipeService.recipeExists(recipeId);

        return ResponseEntity.ok(exist);
    }
}