package com.abnamro.recipes.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;

    private String name;
    private String description;
    private String instruction;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;

    private Boolean vegetarian = false;
    private Integer numberOfServings;

    @PrePersist
    private void prePersist() {
        ingredients.forEach(i -> i.setRecipe(this));
    }
}
