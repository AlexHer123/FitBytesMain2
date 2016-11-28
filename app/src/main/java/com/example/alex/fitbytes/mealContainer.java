package com.example.alex.fitbytes;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Alex on 11/1/2016.
 */

public class mealContainer implements Serializable{
    private int selectedMonth, selectedDate, selectedYear;
    private int recipeID;
    public mealContainer(){
        selectedMonth = -1;
        selectedDate = -1;
        selectedYear = -1;
    }

    public void setDate(int month, int date, int year){
        selectedMonth = month;
        selectedDate = date;
        selectedYear = year;
    }

    public void setRecipe(int ID){
        recipeID = ID;
    }

    public int getMonth(){
        return selectedMonth;
    }

    public int getDate(){
        return selectedDate;
    }

    public int getYear(){
        return selectedYear;
    }

    public int getRecipeID(){
        return recipeID;
    }

    public void printMeal(){
        Log.d("DATE IS: ", ""+selectedYear+selectedMonth+selectedDate);
    }
}
