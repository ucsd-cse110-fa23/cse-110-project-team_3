package cse.project.team_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import cse.project.team_3.client.RecipeList;
import cse.project.team_3.client.Recipe;

public class RecipeListTest {
    private RecipeList recipeList;

    @BeforeEach
    public void init() {
        this.recipeList = new RecipeList();
        Recipe recipe1 = new Recipe("Pizza", "Pizza recipe", "Lunch", "Tomatoes");
        Recipe recipe2 = new Recipe("French Fries", "French Fry recipe", "Lunch", "Potatos");
        Recipe recipe3 = new Recipe("Pasta", "Pasta recipe", "Dinner", "Pasta");
        this.recipeList.add(recipe1);
        this.recipeList.add(recipe2);
        this.recipeList.add(recipe3);
    }

    @Test
    public void testCreateRecipeList(){
        Recipe recipe1 = recipeList.get(0);
        String expectedTitle1 = "Pizza";
        String expectedBody1 = "Pizza recipe";
        String expectedMealType1 = "Lunch";
        String expectedIngredients1 = "Tomatoes";

        Recipe recipe2 = recipeList.get(1);
        String expectedTitle2 = "French Fries";
        String expectedBody2 = "French Fry recipe";
        String expectedMealType2 = "Lunch";
        String expectedIngredients2 = "Potatos";

        Recipe recipe3 = recipeList.get(2);
        String expectedTitle3 = "Pasta";
        String expectedBody3 = "Pasta recipe";
        String expectedMealType3 = "Dinner";
        String expectedIngredients3 = "Pasta";

        assertEquals(expectedTitle1, recipe1.getTitle());
        assertEquals(expectedBody1, recipe1.getBody());
        assertEquals(expectedMealType1, recipe1.getMealType());
        assertEquals(expectedIngredients1, recipe1.getIngredients());

        assertEquals(expectedTitle2, recipe2.getTitle());
        assertEquals(expectedBody2, recipe2.getBody());
        assertEquals(expectedMealType2, recipe2.getMealType());
        assertEquals(expectedIngredients2, recipe2.getIngredients());

        assertEquals(expectedTitle3, recipe3.getTitle());
        assertEquals(expectedBody3, recipe3.getBody());
        assertEquals(expectedMealType3, recipe3.getMealType());
        assertEquals(expectedIngredients3, recipe3.getIngredients());
    }

    @Test
    public void testSize() {
        assertEquals(3, recipeList.size());
    }

    @Test
    public void testGetFromTitle() {
        Recipe recipe = recipeList.get("Pizza");
        String expectedTitle = "Pizza";
        String expectedBody = "Pizza recipe";
        String expectedMealType = "Lunch";
        String expectedIngredients = "Tomatoes";

        assertEquals(expectedTitle, recipe.getTitle());
        assertEquals(expectedBody, recipe.getBody());
        assertEquals(expectedMealType, recipe.getMealType());
        assertEquals(expectedIngredients, recipe.getIngredients());
    }

    @Test
    public void testContains() {
        assertTrue(recipeList.contains("Pizza"));
        assertTrue(recipeList.contains("French Fries"));
        assertTrue(recipeList.contains("Pasta"));
        assertFalse(recipeList.contains("Pancakes"));
    }

    @Test
    public void testRemove() {
        recipeList.remove("Pizza");
        assertEquals(2, recipeList.size());
        assertFalse(recipeList.contains("Pizza"));
    }

    @Test
    public void testClear() {
        recipeList.clear();
        assertEquals(0, recipeList.size());
        assertTrue(recipeList.isEmpty());
    }
}

