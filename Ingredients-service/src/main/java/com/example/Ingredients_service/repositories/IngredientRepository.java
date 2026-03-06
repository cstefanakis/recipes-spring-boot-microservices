package com.example.Ingredients_service.repositories;

import com.example.Ingredients_service.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    @Query("""
            SELECT i.name FROM Ingredient i
            WHERE i.id = :ingredientId
            """)
    Optional<String> findIngredientNameById(@Param("ingredientId") Integer ingredientId);

    @Query("""
            SELECT i FROM Ingredient i
            JOIN i.categoriesId c
            WHERE c = :categoryId
            """)
    List<Ingredient> findAllByCategoryId(@Param("categoryId")Integer categoryId);

    @Query("""
            SELECT i.id FROM Ingredient i
            """)
    List<Integer> findAllIds();

    @Query("""
            SELECT i.id FROM Ingredient i
            JOIN i.categoriesId c
            WHERE c = :categoryId
            """)
    List<Integer> findAllIdByCategoryId(@Param("categoryId")Integer categoryId);
}
