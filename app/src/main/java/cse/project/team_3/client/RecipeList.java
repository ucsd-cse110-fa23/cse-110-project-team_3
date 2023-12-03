package cse.project.team_3.client;

import java.util.ArrayList;
import java.util.List;

/*
 * This class implements the Recipe List data structure
 * It stores recipe objects in an array list and contains several
 * methods for list operations
 */
public class RecipeList {
    List<Recipe> recipeList;

    public RecipeList() {
        recipeList = new ArrayList<Recipe>();
    }

    /*
     * This method checks if the recipe list contains
     * a recipe, based on its title
     * 
     * @param title The title of the recipe that you are searching for
     * @return true if the list contains the recipe or false if not
     */
    public boolean contains(String title) {
        for (int i = 0; i < recipeList.size(); i++) {
            if (title == recipeList.get(i).getTitle()) {
                return true;
            }
        }
        return false;
    }

    /*
     * This method adds a recipe to the recipe list using a recipe object
     * It does not allow repeat recipes to be added (not sure why I added
     * this feature. So remove it if it causes bugs)
     * 
     * @param recipe The recipe to be added
     */
    public void add(Recipe recipe) {
        if (!contains(recipe.getTitle())) {
            this.recipeList.add(recipe);
        }
    }

    /*
     * This method adds a recipe to the recipe list using a string
     * provided by ChatGPT
     * It should parse out all of the information contained in the string
     * 
     * @param recipe The String containing all of the recipe fields
     */
    public void add(String recipe) {
        //TODO: Parse String from ChatGPT into a Recipe Object and Add to recipeList
    }

    /*
     * This method removes a recipe from the recipe list based on its title
     * 
     * @param title The title of the recipe to remove
     */
    public void remove(String title) {
        for (int i = 0; i < recipeList.size(); i++) {
            if (title == recipeList.get(i).getTitle()) {
                recipeList.remove(i);
            }
        }
    }

    /*
     * This method gets the recipe object at a specified index
     * 
     * @param i The index to retrieve a recipe from
     * @return The recipe object that the specified index
     */
    public Recipe get(int i) {
        return recipeList.get(i);
    }

    /*
     * This method get the recipe object with the specified title
     * 
     * @param title The title of the recipe to retrieve
     * @return The recipe object with the specified title
     * @return null if no such recipe exists
     */
    public Recipe get(String title) {
        for (int i = 0; i < recipeList.size(); i++) {
            if (title == recipeList.get(i).getTitle()) {
                return recipeList.get(i);
            }
        }
        return null;
    }

    /*
     * This method returns the size of the recipe list
     * 
     * @return the size of recipeList
     */
    public int size() {
        return recipeList.size();
    }

    /*
     * This method states if the recipe list is empty
     * 
     * @return true if recipeList is empty or false if not
     */
    public boolean isEmpty() {
        return recipeList.isEmpty();
    }

    /*
     * This method removes all recipes from the recipe List
     */
    public void clear() {
        recipeList.clear();
    }
}

