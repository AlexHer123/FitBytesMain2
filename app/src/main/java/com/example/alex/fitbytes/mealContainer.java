package com.example.alex.fitbytes;

/**
 * Created by Alex on 11/1/2016.
 */

public class mealContainer {
    private String meal;
    private String date;
    public mealContainer(String d, String m){
        date = d;
        meal = m;
    }

    public String getMeal(){
        return meal;
    }

    public String getDate(){
        return date;
    }

    public String getAsString(){
        return date + " " + meal;
    }

}
