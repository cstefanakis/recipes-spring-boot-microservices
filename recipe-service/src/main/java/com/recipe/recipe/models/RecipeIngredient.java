package com.recipe.recipe.models;

import com.recipe.recipe.enums.Unit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ingredient_id")
    private Integer ingredientId;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Column(name = "quantity")
    private Double quantity;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
