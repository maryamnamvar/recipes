package com.abnamro.recipes.exception;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException(Long id) {
        super("Recipe " + id + " not found!!");
    }
}
