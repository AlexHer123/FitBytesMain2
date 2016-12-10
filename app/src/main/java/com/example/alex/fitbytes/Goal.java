package com.example.alex.fitbytes;

import java.util.Calendar;

/**
 * Created by ger on 11/19/16.
 */

public abstract class Goal {
    private int date;
    private int dueDate;
    private boolean completed;
    private Category category;
    protected enum Type{ DAILY, WEEKLY, USER }
    private Type type;

    public boolean getCompleted(){
        return completed;
    }
    public String getDescription(){
        return category.getOption();
    }
    public int getDate(){
        return date;
    }
    public int getDueDate(){
        return dueDate;
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
    public void setDueDate(int dueDate){
        this.dueDate = dueDate;
    }
    public void setType(Type type){
        this.type = type;
    }
    public void setDate(int date){
        this.date = date;
    }
    public void setDate(Calendar calendar){
        this.date = Integer.parseInt(String.format("%04d%02d%02d", calendar.get(Calendar.YEAR),
                (calendar.get(Calendar.MONTH)), calendar.get(Calendar.DATE)));
    }
    public String toString(){
        return getDescription() + (getCompleted()? " (Done)" : "");
    }
    public String getDateAsString(int d){
        String dateString = Integer.toString(d);
        String[] arr = {
                dateString.substring(0, 4),
                dateString.substring(4, 6),
                dateString.substring(6, 8)
        };
        String s = String.format("%4s/%2s/%2s", arr[0], arr[1], arr[2]);
        return s;
    }
}