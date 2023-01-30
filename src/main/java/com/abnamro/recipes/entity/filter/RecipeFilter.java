package com.abnamro.recipes.entity.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class RecipeFilter {

    private String name;
    private String instruction;
    private Map<String, String> ingredients;
    private Boolean vegetarian;
    private Integer numberOfServings;
}
