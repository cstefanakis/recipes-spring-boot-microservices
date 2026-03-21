package com.example.recipe_step_service.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "instructions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "step_number")
    private Integer stepNumber;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image", length = 500)
    private String imgUrl;

    @Column(name = "recipe_id")
    private Integer recipeId;
}
