package com.example.alex.fitbytes;

import java.io.Serializable;

/**
 * Created by Alex on 12/9/2016.
 */

public class IngredientItem implements Serializable{
    private String name;
    private int quantity;
    private String measurement;

    public IngredientItem(String n, int q, String m) {
        name = n;
        quantity = q;
        measurement = m;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }


}
