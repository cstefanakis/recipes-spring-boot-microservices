package com.example.recipe_service.services;

import com.example.recipe_service.repositories.RecipeStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeStepService {

    private final RecipeStepRepository recipeStepRepository;

}
