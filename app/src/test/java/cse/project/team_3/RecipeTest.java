package cse.project.team_3;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import cse.project.team_3.client.Recipe;

public class RecipeTest {
    @Test
    public void testDeafultConstructor() {
        Recipe recipe = new Recipe();
        String expectedTitle = "No Title";
        String expectedBody = "No Body";
        String expectedMealType = "No Meal Type";
        String expectedIngredients = "No Ingredients";
        assertEquals(expectedTitle, recipe.getTitle());
        assertEquals(expectedBody, recipe.getBody());
        assertEquals(expectedMealType, recipe.getMealType());
        assertEquals(expectedIngredients, recipe.getIngredients());
    }
    @Test
    public void testSpecifiedConstructor() {
        Recipe recipe = new Recipe("Pizza", "Pizza Recipe", "Lunch", "Dough");
        String expectedTitle = "Pizza";
        String expectedBody = "Pizza Recipe";
        String expectedMealType = "Lunch";
        String expectedIngredients = "Dough";
        assertEquals(expectedTitle, recipe.getTitle());
        assertEquals(expectedBody, recipe.getBody());
        assertEquals(expectedMealType, recipe.getMealType());
        assertEquals(expectedIngredients, recipe.getIngredients());
    }
    @Test
    public void testSetters() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Pizza");
        recipe.setBody("Pizza Recipe");
        recipe.setMealType("Lunch");
        recipe.setIngredients("Dough");
        LocalDateTime expectedDateCreated = LocalDateTime.now();
        recipe.setDateCreated(expectedDateCreated);
        String expectedTitle = "Pizza";
        String expectedBody = "Pizza Recipe";
        String expectedMealType = "Lunch";
        String expectedIngredients = "Dough";
        
        assertEquals(expectedTitle, recipe.getTitle());
        assertEquals(expectedBody, recipe.getBody());
        assertEquals(expectedMealType, recipe.getMealType());
        assertEquals(expectedIngredients, recipe.getIngredients());
        assertEquals(expectedDateCreated, recipe.getDateCreated());
    }
}

