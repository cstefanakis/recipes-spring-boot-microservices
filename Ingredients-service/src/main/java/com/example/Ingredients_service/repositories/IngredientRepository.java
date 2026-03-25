package com.example.Ingredients_service.repositories;

import com.example.Ingredients_service.models.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    @Query("""
            SELECT i FROM Ingredient i
            JOIN i.categories c
            WHERE c.id = :categoryId
            """)
    Page<Ingredient> findAllByCategoryId(@Param("categoryId") Integer categoryId,
                                                                Pageable page);

    @Query("""
            SELECT i FROM Ingredient i
            WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :ingredientName, '%'))
            """)
    Page<Ingredient> findAllByName(@Param("ingredientName") String ingredientName,
                                                                                 Pageable page);
    @Query("""
            SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
            FROM Ingredient i
            WHERE LOWER(i.name) = LOWER(:name)
            """)
    boolean nameExists(@Param("name") String name);
}
