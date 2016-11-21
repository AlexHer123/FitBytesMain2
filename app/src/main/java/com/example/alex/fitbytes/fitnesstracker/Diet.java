package com.example.alex.fitbytes.fitnesstracker;

/**
 * Created by ger on 11/19/16.
 */

public class Diet implements Category {
    public String option;
    public static final String[] DEFAULT_OPTIONS = {
            "meal",
            "calories",
            "weight"
    };
    public Diet(){
        getDefault();
    }
    private String getDefault(){
        String option = DEFAULT_OPTIONS[(int)(Math.random()*DEFAULT_OPTIONS.length-1)];
        String optionDescription;
        switch(option){
            default:
                optionDescription = "Do " + (int)(Math.random()*30+10)+ " " + option;
                break;
        }
        return optionDescription;
    }
}
