package test.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import client.Recipe;
import client.RecipeList;

public class RecipeListTest {
    private RecipeList recipeList;

    @BeforeEach
    public void setUp() {
        // Create a new RecipeList for each test
        recipeList = new RecipeList();
    }

    @Test
    public void testAddRecipe() {
        // Create a mock Recipe
        Recipe mockRecipe = Mockito.mock(Recipe.class);

        // Add the mock Recipe to the RecipeList
        recipeList.getChildren().add(mockRecipe);

        // Use assertions to verify that the RecipeList contains the mock Recipe
        Assertions.assertTrue(recipeList.getChildren().contains(mockRecipe));
    }

    @Test
    public void testUpdateRecipeIndices() {
        // Create a list of mock Recipe objects
        List<Recipe> mockRecipes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Recipe mockRecipe = Mockito.mock(Recipe.class);
            mockRecipes.add(mockRecipe);
        }

        // Add mock Recipes to the RecipeList
        recipeList.getChildren().addAll(mockRecipes);

        // Call the updateRecipeIndices method
        recipeList.updateRecipeIndices();

        // Use assertions to verify that the indices of mock Recipes have been updated
        for (int i = 0; i < mockRecipes.size(); i++) {
            Recipe mockRecipe = mockRecipes.get(i);
            Mockito.verify(mockRecipe).setRecipeIndex(i + 1);
        }
    }

    // Add more test methods for other behaviors of the RecipeList class
}
