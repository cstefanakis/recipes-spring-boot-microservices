package com.example.Ingredients_service.models;

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

    @Column(name = "image")
    private String imgUrl;

    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name = "ingredient_categories",
            joinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Integer> categoriesId = new ArrayList<>();
}
