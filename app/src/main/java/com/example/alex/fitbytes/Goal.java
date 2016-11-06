package com.example.alex.fitbytes;

import java.util.Calendar;

/**
 * Created by Alex on 10/21/2016.
 */

public class Goal {
    private final String[] DEFAULT = {"push ups", "sit ups", "jumping jacks", "squats", "pullups"};
    private String description;
    private int date;
    private int duration;
    public Goal(){
        description = initDefaultGoal();
        date = Calendar.getInstance().get(Calendar.DATE);
    }
    public Goal(String description){
        this(description, Calendar.getInstance().get(Calendar.DATE), 1);
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
        return String.format("%s %s %s",
                "Do",
                (int)(Math.random()*30)+1,
                DEFAULT[(int)(Math.random()*DEFAULT.length-1)]);
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
