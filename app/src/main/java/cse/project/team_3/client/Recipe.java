package cse.project.team_3.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Recipe {
    String title;
    String body;
    String mealType;
    String ingredients;
    String imageURL;
    String imageFileName;
    LocalDateTime dateCreated;

    public Recipe() {
        this.title = "No Title";
        this.body = "No Body";
        this.mealType = "No Meal Type";
        this.ingredients = "No Ingredients";
        this.imageURL = "No Image URL";
        this.imageFileName = "No Image File Name";
        this.dateCreated = LocalDateTime.now();
    }
    public Recipe(String title, String body, String mealType, String ingredients) {
        this.title = title;
        this.body = body;
        this.mealType = mealType;
        this.ingredients = ingredients;
        this.imageURL = "No Image URL";
        this.imageFileName = "No Image File Name";
        this.dateCreated = LocalDateTime.now();
    }
    public Recipe(String fullRecipe) {
        String[] adjustedRecipe = fullRecipe.split(";", 3);
        this.mealType = adjustedRecipe[1];
        this.ingredients = adjustedRecipe[2];
        System.out.println(mealType);
        System.out.println(ingredients);
        String adjustedTitleBody = adjustedRecipe[0].trim();
        String[] split = adjustedTitleBody.split("\n", 3);
        if (split.length >= 3) {
            this.title = (split[0]);
            this.body = (split[2]);
        }
        else {
            this.title = "error handling title";
            this.body = "error handling body";
        }
        this.dateCreated = LocalDateTime.now();
    }
    Recipe(String title, String body, String mealType, String ingredients, String imageURL, String imageFileName) {
        this.title = title;
        this.body = body;
        this.mealType = mealType;
        this.ingredients = ingredients;
        this.imageURL = imageURL;
        this.imageFileName = imageFileName;
        this.dateCreated = LocalDateTime.now();
    }
    Recipe(String[] recipeArray) throws MalformedURLException, IOException, URISyntaxException {
        String[] temp = recipeArray[0].trim().split("\n", 3);
        this.title = temp[0];
        this.body = temp[2];
        String tempMealType = recipeArray[1];
        if ((tempMealType.trim().startsWith("b")) || (tempMealType.trim().startsWith("B"))) {
            this.mealType = "Breakfast";
        }
        else if ((tempMealType.trim().startsWith("l")) || (tempMealType.trim().startsWith("L"))) {
            this.mealType = "Lunch";
        }
        else if ((tempMealType.trim().startsWith("d")) || (tempMealType.trim().startsWith("D"))) {
            this.mealType = "Dinner";
        }
        else {
            this.mealType = "Dinner";
        }
        this. ingredients = recipeArray[2];
        this.imageURL = recipeArray[3];
        handleImageURL();
        this.dateCreated = LocalDateTime.now();
    }
    Recipe(Recipe recipe) {
        this.title = recipe.getTitle();
        this.body = recipe.getBody();
        this.mealType = recipe.getMealType();
        this.ingredients = recipe.getIngredients();
        this.imageFileName = recipe.getImageFileName();
        this.imageURL = recipe.getImageURL();
        this.dateCreated = LocalDateTime.now();
    }

    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }
    public String getMealType() {
        return mealType;
    }
    public String getIngredients() {
        return ingredients;
    }
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
    public String getImageFileName() {
        return imageFileName;
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
    public void setImageURL(String imageURL) throws MalformedURLException, IOException, URISyntaxException {
        this.imageURL = imageURL;
        handleImageURL();
    }
    public void handleImageURL() throws MalformedURLException, IOException, URISyntaxException {
        this.imageFileName = this.title.replace(" ", "_") + ".jpg";
        imageFileName = imageFileName.replace(":", "_");
        try(
            InputStream in = new URI(this.imageURL).toURL().openStream()
        )
        {
            Files.copy(in, Paths.get("Images/" + imageFileName));
        }
    }
}
