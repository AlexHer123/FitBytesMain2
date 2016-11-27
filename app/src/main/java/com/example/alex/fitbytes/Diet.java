package com.example.alex.fitbytes;

/**
 * Created by ger on 11/19/16.
 */

public class Diet extends Category {
    public static final String[] DEFAULT_OPTIONS = {
            "meal",
            "calories",
            "weight"
    };
    public Diet(){
        this.setOption(getDefault());
    }
    public Diet(String option){
        super(option);
    }
    protected String getDefault(){
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
