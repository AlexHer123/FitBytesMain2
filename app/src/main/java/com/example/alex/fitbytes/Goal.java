package com.example.alex.fitbytes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private int id;

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
        d+=100;
        SimpleDateFormat editButtonFormat = new SimpleDateFormat("yyyyMMdd");
        try{
            Date date = editButtonFormat.parse(""+d);
            editButtonFormat = new SimpleDateFormat("MMM dd, yyyy");
//            month = Integer.parseInt(editButtonFormat.format(date));
//            editButtonFormat = new SimpleDateFormat("d");
//            day = Integer.parseInt(editButtonFormat.format(date));
            String fullDate = editButtonFormat.format(date);
//            editButtonFormat = new SimpleDateFormat("yyyy");
//            year = Integer.parseInt(editButtonFormat.format(date));
//            brokenDate[0] = month;
//            brokenDate[1] = day;
//            brokenDate[2] = year;
            return fullDate;

        } catch (ParseException e){e.printStackTrace();}
//        String dateString = Integer.toString(d);
//        String[] arr = {
//                dateString.substring(0, 4),
//                dateString.substring(4, 6),
//                dateString.substring(6, 8)
//        };
//        String s = String.format("%4s/%2s/%2s", arr[0], arr[1]+1, arr[2]);
        return null;
    }

    public boolean canMarkForDelete(){
        return (dueDate < date) && completed;
    }
    public boolean isExpired(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c.getTime());
        int newDate = Integer.parseInt(formattedDate);
        return (dueDate < date) && !completed;
    }
    public void setID(int id){
        this.id = id;
    }
    public int getID(){
        return id;
    }

    //todo remove. used for testing
    public void forceExpired(){
        completed = false;
    }
}