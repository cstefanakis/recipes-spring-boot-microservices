package com.recipe.ingredients.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ingredients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "image", length = 1000)
    private String imgUrl;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "ingredient_category",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();
}
