package com.abnamro.recipes.web.controller;

import com.abnamro.recipes.entity.filter.RecipeFilter;
import com.abnamro.recipes.service.RecipeService;
import com.abnamro.recipes.web.dto.RecipeDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public List<RecipeDto> getAllRecipes(@RequestHeader String userId) {
        return recipeService.getAllRecipes(userId)
                .stream()
                .map(RecipeDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/recipes/filter")
    public List<RecipeDto> filterRecipes(@RequestHeader String userId, @RequestBody RecipeFilter filter) {
        return recipeService.filterRecipes(userId, filter)
                .stream()
                .map(RecipeDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping("/recipes")
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto createRecipe(@Valid @RequestBody RecipeDto recipeDto, @RequestHeader String userId) {
        return RecipeDto.fromEntity(recipeService.createRecipe(recipeDto.toEntity(), userId));
    }

    @PutMapping("/recipes")
    @ResponseStatus(HttpStatus.OK)
    public RecipeDto updateRecipe(@Valid @RequestBody RecipeDto recipeDto, @RequestHeader String userId) {
        return RecipeDto.fromEntity(recipeService.updateRecipe(recipeDto.toEntity(), userId));
    }

    @DeleteMapping("/recipes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@RequestHeader String userId, @PathVariable Long id) {
        recipeService.deleteRecipe(id, userId);
    }
}
