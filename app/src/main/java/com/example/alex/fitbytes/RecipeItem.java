package com.example.alex.fitbytes;

/**
 * Created by Alex on 12/12/2016.
 */

public class RecipeItem {
    private int recipeID;
    private String recipeName;
    private int type; // 0 = Default, 1 = user, 2 = API

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public RecipeItem(int id, String name, int t) {
        recipeID = id;
        recipeName = name;
        type = t;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public String getName() {
        return recipeName;
    }
}
