package com.example.alex.fitbytes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



/**
 * Created by shaun on 10/24/2016.
 */

public class UserProfile  extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
    }


    int height, weight;
    double BMI;
    boolean isHealthyBMI;

    int healthyBMImin = 19;
    int healthyBMImax = 25;
    int BMIconversionFactor = 703;


    public UserProfile(){
         height = 70;
         weight = 180;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public void setHeight(int h) {
        height = h;
    }

    public void setWeight(int w) {
        weight = w;
    }

    public void calculateBMI(){
        BMI = BMIconversionFactor*(weight/(height*height));     // 703*weight/height^2
        if (BMI > healthyBMImin && BMI < healthyBMImax)
            isHealthyBMI = true;
    }
}
