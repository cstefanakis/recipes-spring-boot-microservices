package com.example.recipe_service.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "name")
    private String name;

    @Column(name = "image", length = 500)
    private String imgUrl;

    @Builder.Default
    @ManyToMany(mappedBy = "categories")
    @JsonBackReference
    private List<Recipe> recipes = new ArrayList<>();
}
