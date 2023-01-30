package com.abnamro.recipes.service;

import com.abnamro.recipes.entity.Recipe;
import com.abnamro.recipes.entity.filter.RecipeFilter;
import com.abnamro.recipes.exception.UserNotFoundException;
import com.abnamro.recipes.repository.RecipeRepository;
import com.abnamro.recipes.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    private final String USER_ID = "a@b.com";
    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @Captor
    ArgumentCaptor<Recipe> recipeCaptor;

    @Test
    void canGetAllRecipes() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(recipeRepository.findAll()).thenReturn(List.of(new Recipe()));

        List<Recipe> allRecipes = recipeService.getAllRecipes(USER_ID);
        assertThat(allRecipes).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> recipeService.getAllRecipes(USER_ID));
    }

    @Test
    void canFilterAllRecipes() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(recipeRepository.findAll(any(Specification.class))).thenReturn(List.of(new Recipe()));

        List<Recipe> allRecipes = recipeService.filterRecipes(USER_ID, new RecipeFilter());
        assertThat(allRecipes).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundInFiltering() {
        when(userRepository.existsById(USER_ID)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> recipeService.filterRecipes(USER_ID, new RecipeFilter()));
    }

    @Test
    void canCreateRecipe() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(recipeRepository.save(recipeCaptor.capture())).thenReturn(any());

        recipeService.createRecipe(new Recipe(), USER_ID);
        assertEquals(USER_ID, recipeCaptor.getValue().getUserId());
    }
}