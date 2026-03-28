package com.example.recipe_service.repositories;

import com.example.recipe_service.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("""
            SELECT COUNT(c) > 0 FROM Category c
            WHERE LOWER(c.name) = LOWER(:name)
            """)
    boolean existsByName(@Param("name") String name);
}
