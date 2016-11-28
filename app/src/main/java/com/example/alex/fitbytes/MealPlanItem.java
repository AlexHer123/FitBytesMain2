package com.example.alex.fitbytes;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Alex on 11/27/2016.
 */

public class MealPlanItem implements Serializable {
    private int mpDate;
    private int recipeID;
    private String recipeName;

    public MealPlanItem(){}

    public void setMpDate(int month, int day, int year){
        String stringDate = ""+year;
        if (month < 10) stringDate+=0;
        stringDate+=month;
        if (day < 10) stringDate+=0;
        stringDate+=day;
        mpDate = Integer.parseInt(stringDate);
    }

    public void setRecipe(int id, String name){
        recipeID = id;
        recipeName = name;
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

    public String getRecipeName(){
        return recipeName;
    }

    public void printContents(){
        Log.d("MPI: ", ""+mpDate+" "+recipeID+" "+recipeName);
    }
}
