package com.example.alex.fitbytes;

/**
 * Created by Austin on 11/5/2016.
 */

public class IngredientItem
{
    private String name;
    private float amount;
    private long ingredientID;
    //private Category category;
    private Measurement measurement;

    //public enum Category {VEGETABLE, FRUIT, DAIRY, MEAT, GRAIN, SWEETS}

    public enum Measurement {CUP, OZ, GRAM, LB, TBSP, TSP, FLOZ, NONE}

    public IngredientItem(String name, float amount, String measurement/*, Category category*/)
    {
        this.name = name;
        this.amount = amount;
        this.measurement = Measurement.valueOf(measurement);
        //this.category = category;
        this.ingredientID = 0;
    }

    public IngredientItem(String name, float amount, String measurement/*, Category category*/, int ingredientID)
    {
        this.name = name;
        this.amount = amount;

        this.measurement = Measurement.valueOf(measurement);
        //this.category = category;
        this.ingredientID = ingredientID;
    }

    public String getName()
    {
        return name;
    }

    public float getAmount()
    {
        return amount;
    }

    /*public Category getCategory()
    {
        return category;
    }*/

    public Measurement getMeasurement()
    {
        return measurement;
    }

    public long getIngredientID()
    {
        return ingredientID;
    }

    public String toString()
    {
        return "Name: " + name + " Amount: " + amount + " Measurement: " + measurement /*+ " Category: " + category*/ + " ID: " + ingredientID;
    }
}
