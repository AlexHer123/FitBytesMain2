package com.example.alex.fitbytes.fitnesstracker;

import java.util.Calendar;

/**
 * Created by ger on 11/19/16.
 */

public class Goal {
    private String description;
    private int date;
    private int duration;
    private boolean completed;
    private Category category;

    public boolean getCompleted(){ return completed; }
    public void setCompleted(boolean b){ completed = b; }
    public String getDescription(){return description;}
    public int getDate(){return date;}
    public int getDuration(){return duration; }
    public void setDescription(String description){this.description = description;}
    public void setDate(int date){ this.date = date; }
    public void setDuration(int duration){ this.duration = duration; }

    protected enum Type{
        DAILY, WEEKLY, USER
    }
    private Type type;
    public Goal(Type type){
        description = initDefaultGoal();
        this.type = type;
        setDuration();
        setDescriptionPrefix();
        setDate();
    }
    private void setDuration(){
        switch(type){
            case DAILY:
                duration = 1;
                break;
            case WEEKLY:
                duration = 7;
                break;
        }
    }
    private void setDate(){
        Calendar c = Calendar.getInstance();
        String dateString = "";
        switch(type){
            case DAILY:
                break;
            case WEEKLY:
                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
        }
        dateString = String.format("%s%s%s", c.get(Calendar.YEAR), (c.get(Calendar.MONTH)+1), c.get(Calendar.DATE));
        date = Integer.parseInt(dateString);

    }
    private void setDescriptionPrefix(){
        switch(type){
            case DAILY:
                description = "Daily: " + description;
                break;
            case WEEKLY:
                description = "Weekly: " + description;
                break;
        }
    }
    private void setType(){
        switch(duration){
            case 1:
                type = Type.DAILY;
                break;
            case 7:
                type = Type.WEEKLY;
                break;
            default:
                type = Type.USER;
                break;
        }
    }
    public Goal(String description, int date, int duration){
        this.description = description;
        this.date = date;
        this.duration = duration;
        setType();
    }
    /*public DefaultGoal(int currentDate, int duration, Type type){
        description = initDefaultGoal();
        date = currentDate;
        this.duration = duration;
        this.type = type;
    }*/
    private String initDefaultGoal(){
        String option = Exercise.DEFAULT_OPTIONS[(int)(Math.random()*Exercise.DEFAULT_OPTIONS.length-1)];
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
