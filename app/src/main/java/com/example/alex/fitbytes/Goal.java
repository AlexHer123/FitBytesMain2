package com.example.alex.fitbytes;

import java.util.Calendar;

/**
 * Created by Alex on 10/21/2016.
 */

public class Goal {
    private final String[] DEFAULT_OPTIONS = {
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
    private String description;
    private int date;
    private int duration;
    public Goal(){
        description = initDefaultGoal();
        date = Calendar.getInstance().get(Calendar.DATE);
    }
    public Goal(String description){
        this(description, Calendar.getInstance().get(Calendar.DAY_OF_YEAR), 1);
    }
    public Goal(int date){
        description = initDefaultGoal();
        this.date = date;
        this.duration = 1;
    }
    public Goal(String description, int date, int duration){
        this.description = description;
        this.date = date;
        this.duration = duration;
    }
    private String initDefaultGoal(){
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

    public String getDescription(){return description;}
    public int getDate(){return date;}
    public int getDuration(){return duration; }
    public void setDescription(String description){
        this.description = description;
    }
    public void setDate(int date){
        this.date = date;
    }
    public void setDuration(int duration){ this.duration = duration; }
}
