package com.example.ingredient_categories_service.repositories;


import com.example.ingredient_categories_service.models.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Integer> {

}
