package com.example.recipe_step_service.services;

import com.example.recipe_step_service.clients.RecipeClient;
import com.example.recipe_step_service.dtos.RecipeStepCreateRequestDto;
import com.example.recipe_step_service.dtos.RecipeStepResponseDto;
import com.example.recipe_step_service.dtos.RecipeStepUpdateRequestDto;
import com.example.recipe_step_service.models.RecipeStep;
import com.example.recipe_step_service.repositories.RecipeStepRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RecipeStepServiceTest {

    @InjectMocks
    private RecipeStepService recipeStepService;

    @Mock
    private RecipeStepRepository recipeStepRepository;

    @Mock
    private RecipeClient recipeClient;

    private RecipeStepCreateRequestDto recipeStepCreateRequestDto;
    private RecipeStep savedRecipeStep;
    private RecipeStepUpdateRequestDto recipeStepUpdateRequestDto;
    private RecipeStep updatedRecipeStep;

    @BeforeEach
    void setup(){
        this.recipeStepCreateRequestDto = RecipeStepCreateRequestDto.builder()
                .stepNumber(1)
                .description("description")
                .imgUrl("url")
                .recipeId(1)
                .build();

       this.savedRecipeStep = RecipeStep.builder()
                .id(1)
                .stepNumber(1)
                .description("description")
                .imgUrl("url")
                .recipeId(1)
                .build();

       this.recipeStepUpdateRequestDto = RecipeStepUpdateRequestDto.builder()
               .description("new description")
               .imgUrl("new url")
               .stepNumber(2)
               .build();

        this.updatedRecipeStep = RecipeStep.builder()
                .description(this.recipeStepUpdateRequestDto.getDescription())
                .recipeId(1)
                .imgUrl(this.recipeStepUpdateRequestDto.getImgUrl())
                .stepNumber(this.recipeStepUpdateRequestDto.getStepNumber())
                .id(1)
                .build();
    }

    @Test
    void createRecipeStep() {
        //Arrange
        Integer recipeId = this.recipeStepCreateRequestDto.getRecipeId();
        //Mock
        when(recipeClient.recipeExists(recipeId)).thenReturn(true);
        when(recipeStepRepository.save(any(RecipeStep.class))).thenReturn(this.savedRecipeStep);
        //Act
        recipeStepService.createRecipeStep(this.recipeStepCreateRequestDto);
        //Verify
        verify(recipeClient).recipeExists(any(Integer.class));
        verify(recipeStepRepository).save(any(RecipeStep.class));
    }

    @Test
    void createRecipeStep_withBiggerStepNumberNull() {
        //Arrange
        Integer recipeId = this.recipeStepCreateRequestDto.getRecipeId();
        //Mock
        when(recipeClient.recipeExists(recipeId)).thenReturn(true);
        when(recipeStepRepository.findBiggerRecipeNumberByRecipeId(recipeId)).thenReturn(null);
        when(recipeStepRepository.save(any(RecipeStep.class))).thenReturn(this.savedRecipeStep);
        this.recipeStepCreateRequestDto.setStepNumber(5);
        //Act
        recipeStepService.createRecipeStep(this.recipeStepCreateRequestDto);

        //Verify
        verify(recipeStepRepository).findBiggerRecipeNumberByRecipeId(recipeId);
        verify(recipeClient).recipeExists(any(Integer.class));
        verify(recipeStepRepository).save(argThat(recipeStep ->
                recipeStep.getStepNumber() == 1));
    }

    @Test
    void createRecipeStep_withStepTooBig() {
        //Arrange
        Integer recipeId = this.recipeStepCreateRequestDto.getRecipeId();
        //Mock
        when(recipeClient.recipeExists(recipeId)).thenReturn(true);
        when(recipeStepRepository.findBiggerRecipeNumberByRecipeId(recipeId)).thenReturn(1);
        when(recipeStepRepository.save(any(RecipeStep.class))).thenReturn(this.savedRecipeStep);
        this.recipeStepCreateRequestDto.setStepNumber(5);
        //Act
        recipeStepService.createRecipeStep(this.recipeStepCreateRequestDto);

        //Verify
        verify(recipeStepRepository).findBiggerRecipeNumberByRecipeId(recipeId);
        verify(recipeClient).recipeExists(any(Integer.class));
        verify(recipeStepRepository).save(argThat(recipeStep ->
                recipeStep.getStepNumber() == 2));
    }

    @Test
    void createRecipeStep_withNormalStepNumber() {
        //Arrange
        Integer recipeId = this.recipeStepCreateRequestDto.getRecipeId();
        //Mock
        when(recipeClient.recipeExists(recipeId)).thenReturn(true);
        when(recipeStepRepository.findBiggerRecipeNumberByRecipeId(recipeId)).thenReturn(1);
        when(recipeStepRepository.save(any(RecipeStep.class))).thenReturn(this.savedRecipeStep);
        this.recipeStepCreateRequestDto.setStepNumber(2);
        //Act
        recipeStepService.createRecipeStep(this.recipeStepCreateRequestDto);

        //Verify
        verify(recipeStepRepository).findBiggerRecipeNumberByRecipeId(recipeId);
        verify(recipeClient).recipeExists(any(Integer.class));
        verify(recipeStepRepository).save(argThat(recipeStep ->
                recipeStep.getStepNumber() == 2));
    }

    @Test
    void createRecipeStep_withSameStepNumber() {
        //Arrange
        Integer recipeId = this.recipeStepCreateRequestDto.getRecipeId();
        //Mock
        when(recipeClient.recipeExists(recipeId)).thenReturn(true);
        when(recipeStepRepository.findBiggerRecipeNumberByRecipeId(recipeId)).thenReturn(1);
        when(recipeStepRepository.save(any(RecipeStep.class))).thenReturn(this.savedRecipeStep);
        this.recipeStepCreateRequestDto.setStepNumber(1);
        //Act
        recipeStepService.createRecipeStep(this.recipeStepCreateRequestDto);

        //Verify
        verify(recipeStepRepository).findBiggerRecipeNumberByRecipeId(recipeId);
        verify(recipeClient).recipeExists(any(Integer.class));
        verify(recipeStepRepository).save(argThat(recipeStep ->
                recipeStep.getStepNumber() == 1));
    }

    @Test
    void createRecipeStep_RecipeNotExists() {
        //Arrange
        when(recipeClient.recipeExists(any(Integer.class))).thenReturn(false);
        //Act
        assertThrows(EntityNotFoundException.class, ()->
            recipeStepService.createRecipeStep(this.recipeStepCreateRequestDto)
        );
        //Verify
        verify(recipeClient).recipeExists(any(Integer.class));
    }

    @Test
    void getRecipeStepsByRecipeId() {
        //Arrange
        Integer recipeId = 1;
        List<RecipeStep> recipeSteps = List.of(this.savedRecipeStep);
        when(recipeStepRepository.findRecipeStepsByRecipeId(recipeId)).thenReturn(recipeSteps);
        //Act
        List<RecipeStepResponseDto> result = recipeStepService.getRecipeStepsByRecipeId(recipeId);
        //Assert
        assertNotNull(result);
        assertTrue(result.stream()
                .anyMatch(rs-> rs.getId() == 1));
        assertTrue(result.stream()
                .anyMatch(rs-> rs.getRecipeId() == 1));
        assertTrue(result.stream()
                .anyMatch(rs-> rs.getStepNumber() == 1));
        assertTrue(result.stream()
                .anyMatch(rs-> rs.getDescription().equals("description")));
        assertTrue(result.stream()
                .anyMatch(rs-> rs.getImgUrl().equals("url")));
        //Verify
        verify(recipeStepRepository, times(1)).findRecipeStepsByRecipeId(recipeId);
    }

    @Test
    void deleteRecipeStepById() {
        //Arrange
        Integer recipeStepId = 1;
        //Act
        recipeStepService.deleteRecipeStepById(1);
        //Verify
        verify(recipeStepRepository, times(1)).deleteById(recipeStepId);
    }

    @Test
    void updateRecipeStepById() {
        //Arrange
        Integer recipeStepId = 1;
        when(recipeStepRepository.findById(recipeStepId)).thenReturn(Optional.of(this.savedRecipeStep));
        when(recipeStepRepository.findBiggerRecipeNumberByRecipeId(any(Integer.class))).thenReturn(2);
        when(recipeStepRepository.findRecipeStepIdByRecipeIdAndStepNumber(any(Integer.class), any(Integer.class))).thenReturn(2);
        doNothing().when(recipeStepRepository).updateRecipeStepStepNumber(any(Integer.class), any(Integer.class));
        when(recipeStepRepository.save(any(RecipeStep.class))).thenReturn(this.updatedRecipeStep);
        //Act
        recipeStepService.updateRecipeStepById(recipeStepId, this.recipeStepUpdateRequestDto);
        //verify
        verify(recipeStepRepository, times(1)).findById(recipeStepId);
        verify(recipeStepRepository, times(1)).save(any(RecipeStep.class));
    }

    @Test
    void updateRecipeStepById_notFoundId() {
        //Arrange
        Integer recipeStepId = 1;
        when(recipeStepRepository.findById(recipeStepId)).thenReturn(Optional.empty());
        //Act
        assertThrows(EntityNotFoundException.class, () ->
                recipeStepService.updateRecipeStepById(recipeStepId, this.recipeStepUpdateRequestDto));
        //verify
        verify(recipeStepRepository, times(1)).findById(recipeStepId);
        verify(recipeStepRepository, never()).save(any());
    }

    @Test
    void deleteAllByRecipeId() {
        //Arrange
        Integer recipeId = 1;
        //Mock
        doNothing().when(recipeStepRepository).deleteAllByRecipeId(recipeId);
        //Act
        recipeStepService.deleteAllByRecipeId(recipeId);
        //Verify
        verify(recipeStepRepository).deleteAllByRecipeId(recipeId);
    }
}