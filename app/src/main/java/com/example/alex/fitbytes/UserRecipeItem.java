package com.example.alex.fitbytes;

/**
 * Created by Alex on 12/11/2016.
 */

public class UserRecipeItem {
    private int ID;
    private String name;
    private int serving;
    private int readyMin;
    private int calorie;
    private int fat;
    private int carbs;
    private int sugar;
    private int chol;
    private int sodium;
    private int protein;
    private String ingredients;
    private String directions;
    private String aboutRecipe;

    public UserRecipeItem(String name) {
        ID = -1;
        this.name = name;
        serving=0;
        readyMin = 0;
        calorie = 0;
        fat = 0;
        carbs = 0;
        sugar = 0;
        chol = 0;
        sodium = 0;
        protein = 0;
        ingredients = "";
        directions = "";
        aboutRecipe = "";
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    public int getReadyMin() {
        return readyMin;
    }

    public void setReadyMin(int readyMin) {
        this.readyMin = readyMin;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getChol() {
        return chol;
    }

    public void setChol(int chol) {
        this.chol = chol;
    }

    public int getSodium() {
        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        if (directions.equals("Enter directions")){
            this.directions = "";
        }
        else{
            this.directions = directions;
        }

    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        if (ingredients.equals("Enter Ingredients")) {
            this.ingredients = "";
        }
        else {
            this.ingredients = ingredients;
        }
    }

    public String getAboutRecipe() {
        return aboutRecipe;
    }

    public void setAboutRecipe(String aboutRecipe) {
        if (aboutRecipe.equals("Enter recipe information")){
            this.aboutRecipe = "";
        }
        else{
            this.aboutRecipe = aboutRecipe;
        }
    }
}
