package com.abnamro.recipes.service;

import com.abnamro.recipes.entity.Ingredient;
import com.abnamro.recipes.entity.Recipe;
import com.abnamro.recipes.entity.filter.RecipeFilter;
import com.abnamro.recipes.exception.RecipeNotFoundException;
import com.abnamro.recipes.exception.UserNotFoundException;
import com.abnamro.recipes.repository.RecipeRepository;
import com.abnamro.recipes.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public List<Recipe> getAllRecipes(String userId) {
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);

        return recipeRepository.findAll();
    }

    public List<Recipe> filterRecipes(String userId, RecipeFilter filter) {
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);

        return recipeRepository.findAll(buildSpecification(filter));
    }

    private Specification<Recipe> buildSpecification(RecipeFilter filter) {
        return Specification.where(buildNameSpec(filter.getName())
                .and(buildInstructionSpec(filter.getInstruction()))
                .and(buildVegetarianSpec(filter.getVegetarian()))
                .and(buildNumberOfServings(filter.getNumberOfServings()))
                .and(buildIngredientsInclude(filter.getIngredients())))
                .and(buildIngredientsExclude(filter.getIngredients()));
    }

    private Specification<Recipe> buildNameSpec(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    private Specification<Recipe> buildInstructionSpec(String instruction) {
        if (instruction == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("instruction")), "%" + instruction.toLowerCase() + "%");
    }

    private Specification<Recipe> buildVegetarianSpec(Boolean vegetarian) {
        if (vegetarian == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("vegetarian"), vegetarian);
    }

    private Specification<Recipe> buildNumberOfServings(Integer numberOfServings) {
        if (numberOfServings == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("numberOfServings"), numberOfServings);
    }

    private Specification<Recipe> buildIngredientsInclude(Map<String, String> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) return null;
        List<String> include = ingredients.entrySet().stream().filter(e -> e.getValue().equals("in")).map(Map.Entry::getKey).collect(Collectors.toList());
        return (root, query, criteriaBuilder) -> root.join("ingredients").get("name").in(include);
    }

    private Specification<Recipe> buildIngredientsExclude(Map<String, String> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) return null;
        List<String> exclude = ingredients.entrySet().stream().filter(e -> e.getValue().equals("ex")).map(Map.Entry::getKey).collect(Collectors.toList());
        return (root, query, criteriaBuilder) -> {
            Subquery<Recipe> subQuery = query.subquery(Recipe.class);
            Root<Ingredient> ingredientRoot = subQuery.from(Ingredient.class);
            Join<Ingredient, Recipe> join = ingredientRoot.join("recipe");
            subQuery.select(join).where(ingredientRoot.get("name").in(exclude));
            return criteriaBuilder.in(root).value(subQuery).not();
        };
    }

    public Recipe createRecipe(Recipe newRecipe, String userId) {
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);

        newRecipe.setUserId(userId);
        return recipeRepository.save(newRecipe);
    }

    public Recipe updateRecipe(Recipe updateRecipe, String userId) {
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);
        if (updateRecipe.getId() == null) throw new RuntimeException("You should insert id");
        if (!recipeRepository.existsById(updateRecipe.getId())) throw new RecipeNotFoundException(updateRecipe.getId());

        updateRecipe.setUserId(userId);
        return recipeRepository.save(updateRecipe);
    }

    public void deleteRecipe(Long recipeId, String userId) {
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);
        if (!recipeRepository.existsById(recipeId)) throw new RecipeNotFoundException(recipeId);

        recipeRepository.deleteById(recipeId);
    }
}
