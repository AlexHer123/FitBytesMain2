package com.example.alex.fitbytes;

/**
 * Created by Austin on 11/5/2016.
 */

public class PantryItem
{
    private String name;
    private float amount;
    private long ingredientID;
    private Measurement measurement;

    public enum Measurement {CUP, OZ, GRAM, LB, TBSP, TSP, FLOZ, NONE}

    public PantryItem(String name, float amount, String measurement/*, Category category*/)
    {
        this.name = name;
        this.amount = amount;
        this.measurement = Measurement.valueOf(measurement);
        this.ingredientID = 0;
    }

    public PantryItem(String name, float amount, String measurement, int ingredientID)
    {
        this.name = name;
        this.amount = amount;

        this.measurement = Measurement.valueOf(measurement);
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
        return "Name: " + name + " Amount: " + amount + " Measurement: " + measurement + " ID: " + ingredientID;
    }
}
