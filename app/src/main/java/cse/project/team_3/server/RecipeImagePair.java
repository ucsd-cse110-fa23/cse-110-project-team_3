package cse.project.team_3.server;

public class RecipeImagePair {

    // Store string for recipe, and string for recipe image URL
    private String recipe;
    private String imageUrl;

    public RecipeImagePair(String recipe, String imageUrl) {
        this.recipe = recipe;
        this.imageUrl = imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getRecipe() {
        return recipe;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}