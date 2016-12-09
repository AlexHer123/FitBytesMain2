package com.example.alex.fitbytes;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 11/27/2016.
 */

public class MealPlanItem implements Serializable {
    private int mpDate;
    private int recipeID;
//    private String recipeName;
    private Map<Integer, String> recipes;

    public MealPlanItem(){
        recipes = new HashMap<>();
    }

    public void setMpDate(int month, int day, int year){
        String stringDate = ""+year;
        if (month < 10) stringDate+=0;
        stringDate+=month;
        if (day < 10) stringDate+=0;
        stringDate+=day;
        mpDate = Integer.parseInt(stringDate);
    }

//    public void setRecipe(int id, String name){
//        recipeID = id;
//        recipeName = name;
//    }

    public boolean addRecipe(int id, String name){
        if (recipes.size() >= 3)
        {
            return false;
        }
        recipes.put(id, name);
        return true;
    }

    public void removeRecipe(String name){
        List<Map.Entry<Integer, String>> entries = new ArrayList<>(recipes.entrySet());
        for (Map.Entry<Integer, String> entry : entries){
            if (entry.getValue().equals(name)) {
                recipes.remove(entry.getKey());
                break;
            }
        }

    }

    public Map<Integer, String> getRecipes(){
        return recipes;
    }
    public ArrayList<String> getRecipeNames(){
        return new ArrayList<String>(recipes.values());
    }

    public int getSize(){
        return recipes.size();
    }
    public void setMpDate(int fullDate){
        mpDate = fullDate;
    }

    public int getDate(){
        return mpDate;
    }

    public int getRecipeID(){
        return recipeID;
    }

//    public String getRecipeName(){
//        return recipeName;
//    }

    public void printContents(){
//        Log.d("MPI: ", ""+mpDate+" "+recipeID+" "+ recipeName);
        Log.d("MPI LIST: ", recipes.entrySet().toString());
    }
}
