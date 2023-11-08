package RecipeView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RecipeGeneration {
    public static String generateFakeRecipe() throws IOException {
        Path path = Paths.get("recipe.txt");
        String output = Files.readString(path);
        return output;
    }
    public static String[] generateFakeRecipeTitle() throws IOException {
        String[] recipeArray = new String[2];
        Path path = Paths.get("recipe.txt");
        recipeArray[1] = Files.readString(path);
        recipeArray[0] = "Homemade Vanilla Pudding";
        return recipeArray;
    }
}
