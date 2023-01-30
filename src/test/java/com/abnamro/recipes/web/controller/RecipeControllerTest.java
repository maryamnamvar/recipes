package com.abnamro.recipes.web.controller;

import com.abnamro.recipes.entity.Ingredient;
import com.abnamro.recipes.entity.Recipe;
import com.abnamro.recipes.repository.RecipeRepository;
import com.abnamro.recipes.web.dto.RecipeDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;
    private static HttpHeaders headers;
    private static final String USER_ID = "mary@gmail.com";

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();

        headers = new HttpHeaders();
        headers.set("userId", USER_ID);
    }

    @BeforeEach
    void setUp() {
        baseUrl = baseUrl + ":" + port + "/api/v1/recipes";
        Recipe entity = new Recipe();
        entity.setName("Fesenjoon");
        entity.setIngredients(List.of(new Ingredient("Gerdoo"), new Ingredient("Morgh")));
        entity.setNumberOfServings(20);
        entity.setVegetarian(true);
        entity.setInstruction("XYZ");
        recipeRepository.save(entity);
    }

    @AfterEach
    void tearDown() {
        recipeRepository.deleteAll();
    }

    @Test
    void getAllRecipes() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        List<Recipe> list = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Recipe>>() {
        }).getBody();
        
        assertEquals(1, list.size());
    }

    @Test
    void createRecipe() {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("Gheyme Bademjoon");
        recipeDto.setIngredients(List.of("Eggplant", "Tomato pure", "Meat"));
        recipeDto.setNumberOfServings(4);
        recipeDto.setVegetarian(false);
        recipeDto.setInstruction("XYZ");

        HttpEntity<RecipeDto> requestEntity = new HttpEntity<>(recipeDto, headers);

        Recipe newRecipe = restTemplate.exchange(baseUrl, POST, requestEntity, Recipe.class).getBody();

        assertNotNull(newRecipe);
        assertThat(newRecipe.getId()).isNotNull();
    }
}