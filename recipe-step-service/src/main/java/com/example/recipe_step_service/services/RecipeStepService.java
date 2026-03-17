package com.example.recipe_step_service.services;

import com.example.recipe_step_service.dtos.RecipeStepCreateRequestDto;
import com.example.recipe_step_service.dtos.RecipeStepResponseDto;
import com.example.recipe_step_service.dtos.RecipeStepUpdateRequestDto;
import com.example.recipe_step_service.models.RecipeStep;
import com.example.recipe_step_service.repositories.RecipeStepRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeStepService {

    private final RecipeStepRepository recipeStepRepository;

    public void createRecipeStep(@Valid RecipeStepCreateRequestDto recipeStepCreateRequestDto) {

        RecipeStep recipeStep = toEntity(recipeStepCreateRequestDto);

        recipeStepRepository.save(recipeStep);
    }

    private RecipeStep toEntity(@Valid RecipeStepCreateRequestDto recipeStepCreateRequestDto) {
        return RecipeStep.builder()
                .stepNumber(recipeStepCreateRequestDto.getStepNumber())
                .description(recipeStepCreateRequestDto.getDescription())
                .imgUrl(recipeStepCreateRequestDto.getImgUrl())
                .build();
    }

    public List<RecipeStepResponseDto> getRecipeStepsByRecipeId(Integer recipeId) {
        List<RecipeStep> recipeSteps = recipeStepRepository.findRecipeStepsByRecipeId(recipeId);
        return recipeSteps.stream()
                .map(this::toRecipeStepResponseDto)
                .toList();
    }

    private RecipeStepResponseDto toRecipeStepResponseDto(RecipeStep recipeStep) {
        return RecipeStepResponseDto.builder()
                .recipeStepId(recipeStep.getId())
                .description(recipeStep.getDescription())
                .imgUrl(recipeStep.getImgUrl())
                .stepNumber(recipeStep.getStepNumber())
                .recipeId(recipeStep.getRecipeId())
                .build();
    }

    public void deleteRecipeStepById(Integer id) {
        recipeStepRepository.deleteById(id);
    }

    public void updateRecipeStepById(Integer id, RecipeStepUpdateRequestDto recipeStepUpdateRequestDto) {

        RecipeStep recipeStep = getRecipeStepById(id);

        recipeStepRepository.save(toUpdateEntity(recipeStep, recipeStepUpdateRequestDto));

    }

    private RecipeStep toUpdateEntity(RecipeStep recipeStep, RecipeStepUpdateRequestDto recipeStepUpdateRequestDto) {

        Integer stepNumberDto = recipeStepUpdateRequestDto.getStepNumber();
        String imgUrlDto = recipeStepUpdateRequestDto.getImgUrl();
        String descriptionDto = recipeStepUpdateRequestDto.getDescription();

        recipeStep.setStepNumber(stepNumberDto == null
                        ? recipeStep.getStepNumber()
                        : stepNumberDto);

        recipeStep.setDescription(descriptionDto == null
                ? recipeStep.getDescription()
                : descriptionDto);

        recipeStep.setImgUrl(imgUrlDto == null
                ? recipeStep.getImgUrl()
                : imgUrlDto);

        return recipeStep;
    }

    private RecipeStep getRecipeStepById(Integer id) {
        return recipeStepRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Recipe step with id: %s not found", id)));
    }
}
