package cse.project.team_3.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 * This class implements the Recipe List data structure
 * It stores recipe objects in an array list and contains several
 * methods for list operations
 */
public class RecipeList {
    List<Recipe> recipeList;
    List<Recipe> visibleRecipeList;
    String sortTag;
    String filterTag;

    public RecipeList() {
        this.recipeList = new ArrayList<Recipe>();
        this.visibleRecipeList = new ArrayList<Recipe>();
        this.sortTag = "First Created";
        this.filterTag = "All";
    }

    public void setSortTag(String sortTag) {
        this.sortTag = sortTag;
    }

    public void setFilterTag(String filterTag) {
        this.filterTag = filterTag;
    }

    public List<Recipe> getVisibleRecipeList() {
        return visibleRecipeList;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    /*
     * This method checks if the recipe list contains
     * a recipe, based on its title
     * 
     * @param title The title of the recipe that you are searching for
     * 
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
        // TODO: Parse String from ChatGPT into a Recipe Object and Add to recipeList
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
     * 
     * @return The recipe object that the specified index
     */
    public Recipe get(int i) {
        return recipeList.get(i);
    }

    /*
     * This method get the recipe object with the specified title
     * 
     * @param title The title of the recipe to retrieve
     * 
     * @return The recipe object with the specified title
     * 
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

    public Recipe getVisible(int i) {
        return visibleRecipeList.get(i);
    }

    public Recipe getVisible(String title) {
        for (int i = 0; i < visibleRecipeList.size(); i++) {
            if (title == visibleRecipeList.get(i).getTitle()) {
                return visibleRecipeList.get(i);
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

    public int visibleSize() {
        return visibleRecipeList.size();
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

    static class CompareAlphabetically implements Comparator<Recipe> {

        @Override
        public int compare(Recipe recipe1, Recipe recipe2) {
            return recipe1.getTitle().compareTo(recipe2.getTitle());
        }

    }

    static class CompareFirstCreated implements Comparator<Recipe> {

        @Override
        public int compare(Recipe recipe1, Recipe recipe2) {
            return recipe1.getDateCreated().compareTo(recipe2.getDateCreated());
        }

    }

    public void sort() {
        switch (sortTag) {
            case "A-Z":
                sortAlphabetically();
                break;
            case "Z-A":
                sortAlphabeticallyReverse();
                break;
            case "First Created":
                sortFirstCreated();
                break;
            case "Last Created":
                sortLastCreated();
                break;
        }
    }

    public void sortAlphabetically() {
        Collections.sort(recipeList, new CompareAlphabetically());
    }

    public void sortAlphabeticallyReverse() {
        Collections.sort(recipeList, new CompareAlphabetically().reversed());
    }

    public void sortFirstCreated() {
        Collections.sort(recipeList, new CompareFirstCreated());
    }

    public void sortLastCreated() {
        Collections.sort(recipeList, new CompareFirstCreated().reversed());
    }

    public void filter() {
        switch (filterTag) {
            case "Breakfast":
                filterBreakfast();
                break;
            case "Lunch":
                filterLunch();
                break;
            case "Dinner":
                filterDinner();
                break;
            case "All":
                filterAll();
                break;
        }
    }

    private void filterAll() {
        visibleRecipeList.clear();
        for (int i = 0; i < recipeList.size(); i++) {
            visibleRecipeList.add(recipeList.get(i));
        }
    }

    public void filterBreakfast() {
        visibleRecipeList.clear();
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getMealType().equals("Breakfast")) {
                visibleRecipeList.add(recipeList.get(i));
            }
        }
    }

    public void filterLunch() {
        visibleRecipeList.clear();
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getMealType().equals("Lunch")) {
                visibleRecipeList.add(recipeList.get(i));
            }
        }
    }

    public void filterDinner() {
        visibleRecipeList.clear();
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getMealType().equals("Dinner")) {
                visibleRecipeList.add(recipeList.get(i));
            }
        }
    }
}
