package com.example.alex.fitbytes;

import java.io.Serializable;

/**
 * Created by Alex on 12/8/2016.
 */

public class UserItem implements Serializable{


    private String name;
    private int height, weight;
    private double BMI;
    private int cals, fat, carbs, sugar, chol, sodium, protein;

    public UserItem(String n, int h, int w, double b, int myCals, int myFat, int myCarbs, int mySugar, int myChol, int mySodium, int myProtein) {
        name = n;
        height = h;
        weight = w;
        BMI = b;

        cals = myCals;
        fat = myFat;
        carbs = myCarbs;
        sugar = mySugar;
        chol = myChol;
        sodium = mySodium;
        protein = myProtein;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public double getBMI() {
        return BMI;
    }

    public int getCals() { return cals; }

    public int getFat() { return fat; }

    public int getCarbs(){ return carbs; }

    public int getSugar() { return sugar; }

    public int getChol() { return chol; }

    public int getProtein() { return protein; }

    public int getSodium() { return sodium; }
}
