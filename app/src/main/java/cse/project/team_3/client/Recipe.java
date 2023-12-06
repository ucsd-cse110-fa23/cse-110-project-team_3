package cse.project.team_3.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.nio.file.Files;

public class Recipe {
    String title;
    String body;
    String mealType;
    String ingredients;
    LocalDateTime dateCreated;
    String imageURL;
    String imageFileName;

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
    public Recipe(String fullRecipe) throws IOException, URISyntaxException {
        System.out.println(fullRecipe);
        String[] adjustedRecipe = fullRecipe.split(";", 4);
        this.mealType = adjustedRecipe[1];
        switch (mealType) {
            case "breakfast":
                this.mealType = "Breakfast";
                break;
            case "lunch":
                this.mealType = "Lunch";
                break;
            case "dinner":
                this.mealType = "Dinner";
                break;
            default:
                this.mealType = "Dinner";
                break;
        }
        this.ingredients = adjustedRecipe[3];
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
        this.imageURL = adjustedRecipe[2];
        this.imageFileName = urlToImage(imageURL);
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

    public String urlToImage(String imageURL) throws IOException, URISyntaxException {
        //String fname = String.format("%s.%s", RandomStringUtils.random(4), "jpg");
        String fname = title + ".jpg"; 
        fname = fname.replace(" ", "_");
        File imageCopy = new File(fname); 
        // Download the Generated Image to Current Directory
        try(InputStream in = new URI(imageURL).toURL().openStream())
        {          
            Files.copy(in,Paths.get(fname));
        }
        return fname;
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
