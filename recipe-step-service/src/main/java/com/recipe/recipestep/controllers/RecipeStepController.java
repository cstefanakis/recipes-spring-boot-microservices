package com.recipe.recipestep.controllers;

import com.recipe.recipestep.dtos.recipeStep.RecipeStepCreateRequestDto;
import com.recipe.recipestep.dtos.recipeStep.RecipeStepResponseDto;
import com.recipe.recipestep.dtos.recipeStep.RecipeStepUpdateRequestDto;
import com.recipe.recipestep.services.RecipeStepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/recipe-steps")
@RequiredArgsConstructor
public class RecipeStepController {

    private final RecipeStepService recipeStepService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecipeStep(@Valid @RequestBody RecipeStepCreateRequestDto recipeStepCreateRequestDto){
        recipeStepService.createRecipeStep(recipeStepCreateRequestDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<RecipeStepResponseDto>> getRecipeStepsByRecipeId(@PathVariable ("recipeId") Integer recipeId){
        List<RecipeStepResponseDto> recipeStepsDto = recipeStepService.getRecipeStepsByRecipeId(recipeId);
        return ResponseEntity.ok(recipeStepsDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipeStepById(@PathVariable("id") Integer id){
        recipeStepService.deleteRecipeStepById(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/delete-all/{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByRecipeId(@PathVariable("recipeId") Integer recipeId){
        recipeStepService.deleteAllByRecipeId(recipeId);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRecipeStepById(@PathVariable("id") Integer id,
                                     @Valid @RequestBody RecipeStepUpdateRequestDto recipeStepUpdateRequestDto){
        recipeStepService.updateRecipeStepById(id, recipeStepUpdateRequestDto);
    }
}
