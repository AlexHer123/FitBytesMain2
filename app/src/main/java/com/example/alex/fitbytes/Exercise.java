package com.example.alex.fitbytes;

/**
 * Created by ger on 11/19/16.
 */

public class Exercise extends Category{
    public static final String[] DEFAULT_OPTIONS = {
            "push ups",
            "sit ups",
            "jumping jacks",
            "squats",
            "pullups",
            "running",
            "walking",
            "crunches",
            "plank"
    };
    public Exercise(){
        this.setOption(getDefault());
    }
    public Exercise(String option){
        super(option);
    }
    protected String getDefault(){
        String option = DEFAULT_OPTIONS[(int)(Math.random()*DEFAULT_OPTIONS.length-1)];
        String optionDescription;
        switch(option){
            case "walking":
                optionDescription = "Take a walk for 20 minutes";
                break;
            case "running":
                optionDescription = "Run for 2 miles";
                break;
            case "plank":
                optionDescription = "Plank for 5 minutes";
                break;
            default:
                optionDescription = "Do " + (int)(Math.random()*30+10)+ " " + option;
                break;
        }
        return optionDescription;
    }
}