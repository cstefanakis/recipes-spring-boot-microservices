package com.example.recipe_step_service.services;

import com.example.recipe_step_service.clients.RecipeClient;
import com.example.recipe_step_service.dtos.recipeStep.RecipeStepCreateRequestDto;
import com.example.recipe_step_service.dtos.recipeStep.RecipeStepResponseDto;
import com.example.recipe_step_service.dtos.recipeStep.RecipeStepUpdateRequestDto;
import com.example.recipe_step_service.models.RecipeStep;
import com.example.recipe_step_service.repositories.RecipeStepRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeStepService {

    private final RecipeStepRepository recipeStepRepository;
    private final RecipeClient recipeClient;
    private final UserService userService;

    @Transactional
    public void createRecipeStep(@Valid RecipeStepCreateRequestDto recipeStepCreateRequestDto) {

        Integer recipeId = recipeStepCreateRequestDto.getRecipeId();

        Integer recipeOwnerId = recipeClient.getRecipeOwnerIdByRecipeId(recipeId).getBody();

        boolean isOwnerOrAdmin = userService.isOwnerOrAdmin(recipeOwnerId);

        if(isOwnerOrAdmin) {
            Integer stepNumber = recipeStepCreateRequestDto.getStepNumber();
            boolean recipeExist = recipeClient.recipeExists(recipeId);

            if (recipeExist) {
                recipeStepCreateRequestDto.setStepNumber(validatedStepNumber(stepNumber, recipeId));
                RecipeStep recipeStep = toEntity(recipeStepCreateRequestDto);
                recipeStepRepository.shiftStepsForward(recipeStep.getStepNumber(), recipeStep.getRecipeId());
                recipeStepRepository.save(recipeStep);
            } else {
                throw new EntityNotFoundException(String.format("Recipe with id %s not exists", recipeId));
            }
        } else {
            throw new RuntimeException("Not Permission");
        }
    }

    private Integer validatedStepNumber(Integer stepNumber, Integer recipeId) {
        Integer biggerRecipeStep = recipeStepRepository.findBiggerRecipeNumberByRecipeId(recipeId);
        if(biggerRecipeStep == null){
            return 1;
        }
        if(stepNumber > biggerRecipeStep + 2){
            return biggerRecipeStep + 1;
        }
        return stepNumber;
    }

    private RecipeStep toEntity(@Valid RecipeStepCreateRequestDto recipeStepCreateRequestDto) {

        return RecipeStep.builder()
                .stepNumber(recipeStepCreateRequestDto.getStepNumber())
                .description(recipeStepCreateRequestDto.getDescription())
                .imgUrl(recipeStepCreateRequestDto.getImgUrl())
                .recipeId(recipeStepCreateRequestDto.getRecipeId())
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
                .id(recipeStep.getId())
                .description(recipeStep.getDescription())
                .imgUrl(recipeStep.getImgUrl())
                .stepNumber(recipeStep.getStepNumber())
                .recipeId(recipeStep.getRecipeId())
                .build();
    }

    public void deleteRecipeStepById(Integer recipeStepId) {

        Integer recipeId = recipeStepRepository.findRecipeIdByRecipeStepId(recipeStepId);

        Integer recipeOwnerId = recipeClient.getRecipeOwnerIdByRecipeId(recipeId).getBody();

        boolean isOwnerOrAdmin = userService.isOwnerOrAdmin(recipeOwnerId);

        if(isOwnerOrAdmin) {
            Integer stepNumber = recipeStepRepository.findRecipeStepByRecipeStepId(recipeStepId);
            recipeStepRepository.shiftStepsBackward(stepNumber, recipeId);
            recipeStepRepository.deleteById(recipeStepId);
        } else {
            throw new RuntimeException("No permission");
        }
    }

    public void updateRecipeStepById(Integer recipeStepId, RecipeStepUpdateRequestDto recipeStepUpdateRequestDto) {

        Integer recipeId = recipeStepRepository.findRecipeIdByRecipeStepId(recipeStepId);

        Integer recipeOwnerId = recipeClient.getRecipeOwnerIdByRecipeId(recipeId).getBody();

        boolean isOwnerOrAdmin = userService.isOwnerOrAdmin(recipeOwnerId);

        if(isOwnerOrAdmin) {
            RecipeStep recipeStep = getRecipeStepById(recipeStepId);
            Integer stepNumber = recipeStep.getStepNumber();
            Integer updatedStepNumber = recipeStepUpdateRequestDto.getStepNumber();

            validateUpdateStepNumber(recipeId, updatedStepNumber);

            exchangeStepNumber(recipeId, stepNumber, updatedStepNumber);

            recipeStepRepository.save(toUpdateEntity(recipeStep, recipeStepUpdateRequestDto));
        } else {
            throw new RuntimeException("No permission");
        }

    }

    private void validateUpdateStepNumber(Integer recipeId, Integer updatedStepNumber) {
        Integer biggerStepNumber = recipeStepRepository.findBiggerRecipeNumberByRecipeId(recipeId);
        if(updatedStepNumber > biggerStepNumber){
            throw new RuntimeException(String.format("Step number can't be bigger as %s", biggerStepNumber));
        }
    }

    private void exchangeStepNumber(Integer recipeId, Integer stepNumber, Integer updatedStepNumber) {
        if(!stepNumber.equals(updatedStepNumber)) {
            Integer recipeStepId = recipeStepRepository.findRecipeStepIdByRecipeIdAndStepNumber(recipeId, updatedStepNumber);
            recipeStepRepository.updateRecipeStepStepNumber(recipeStepId, stepNumber);
        }
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

    @Transactional
    public void deleteAllByRecipeId(Integer recipeId) {

        Integer recipeOwnerId = recipeClient.getRecipeOwnerIdByRecipeId(recipeId).getBody();

        boolean isOwnerOrAdmin = userService.isOwnerOrAdmin(recipeOwnerId);

        if(isOwnerOrAdmin) {
            recipeStepRepository.deleteAllByRecipeId(recipeId);
        } else {
            throw new RuntimeException("No permission");
        }
    }
}
