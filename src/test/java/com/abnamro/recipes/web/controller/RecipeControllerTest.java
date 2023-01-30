package com.abnamro.recipes.web.controller;

import com.abnamro.recipes.entity.Ingredient;
import com.abnamro.recipes.entity.Recipe;
import com.abnamro.recipes.repository.RecipeRepository;
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

import static org.junit.jupiter.api.Assertions.*;

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
    }
}