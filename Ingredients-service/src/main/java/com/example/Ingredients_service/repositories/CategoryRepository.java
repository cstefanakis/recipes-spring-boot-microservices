package com.example.Ingredients_service.repositories;

import com.example.Ingredients_service.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("""
            SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
            FROM Category c
            WHERE LOWER(c.name) = LOWER(:name)
            """)
    boolean nameExists(@Param("name")String name);
}
