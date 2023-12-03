package cse.project.team_3.client;

public class Recipe {
    String title;
    String body;
    String mealType;
    String ingredients;
    String dateCreated;

    public Recipe() {
        this.title = "No Title";
        this.body = "No Body";
        this.mealType = "No Meal Type";
        this.ingredients = "No Ingredients";
        this.dateCreated = java.time.LocalDate.now().toString();
    }
    public Recipe(String title, String body, String mealType, String ingredients) {
        this.title = title;
        this.body = body;
        this.mealType = mealType;
        this.ingredients = ingredients;
        this.dateCreated = java.time.LocalDate.now().toString();
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
    public String getDateCreated() {
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
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
