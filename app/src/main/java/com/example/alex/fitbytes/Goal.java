package com.example.alex.fitbytes;

/**
 * Created by ger on 11/19/16.
 */

public abstract class Goal {
    private int date;
    private int duration;
    private boolean completed;
    private Category category;
    protected enum Type{ DAILY, WEEKLY, USER }
    private Type type;
    public Goal(){};
    public Goal(Category c){
        category = c;
    }
    public boolean getCompleted(){
        return completed;
    }
    public String getDescription(){
        return category.getOption();
    }
    public int getDate(){
        return date;
    }
    public int getDuration(){
        return duration;
    }
    public Type getType(){
        return type;
    }
    public void setCategory(Category c){
        if(c != null) category = c;
    }
    public void setCompleted(boolean value){
        completed = value;
    }
    public void setDescription(String description){
        category.setOption(description);
    }
    public void setDuration(int duration){
        this.duration = duration;
    }
    public void setType(Type type){
        this.type = type;
    }
    public void setDate(int date){
        this.date = date;
    }
}