package com.example.alex.fitbytes;

import java.io.Serializable;

/**
 * Created by Alex on 12/8/2016.
 */

public class UserItem implements Serializable{


    private String name;
    private int height, weight;
    private double BMI;

    public UserItem(String n, int h, int w, double b) {
        name = n;
        height = h;
        weight = w;
        BMI = b;
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
}
