package com.abnamro.recipes.web.dto;

import com.abnamro.recipes.entity.Ingredient;
import com.abnamro.recipes.entity.Recipe;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RecipeDto {

    private Long id;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private String instruction;
    @NotNull
    @NotEmpty
    private List<String> ingredients;
    private Boolean vegetarian = false;
    @NotNull
    private Integer numberOfServings;

    public static RecipeDto fromEntity(Recipe recipe) {
        RecipeDto dto = new RecipeDto();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setInstruction(recipe.getInstruction());
        dto.setVegetarian(recipe.getVegetarian());
        dto.setIngredients(recipe.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList()));
        dto.setNumberOfServings(recipe.getNumberOfServings());
        return dto;
    }

    public Recipe toEntity() {
        Recipe entity = new Recipe();
        entity.setId(this.getId());
        entity.setName(this.getName());
        entity.setVegetarian(this.getVegetarian());
        entity.setInstruction(this.getInstruction());
        entity.setDescription(this.getDescription());
        entity.setIngredients(this.getIngredients().stream().map(Ingredient::new).collect(Collectors.toList()));
        entity.setNumberOfServings(this.getNumberOfServings());
        return entity;
    }
}
