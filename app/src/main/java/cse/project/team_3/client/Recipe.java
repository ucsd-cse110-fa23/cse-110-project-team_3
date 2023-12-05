package cse.project.team_3.client;

import java.time.LocalDateTime;

public class Recipe {
    String title;
    String body;
    String mealType;
    String ingredients;
    LocalDateTime dateCreated;

    public Recipe() {
        this.title = "No Title";
        this.body = "No Body";
        this.mealType = "No Meal Type";
        this.ingredients = "No Ingredients";
        this.dateCreated = LocalDateTime.now();
    }
    public Recipe(String title, String body, String mealType, String ingredients) {
        this.title = title;
        this.body = body;
        this.mealType = mealType;
        this.ingredients = ingredients;
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
}
