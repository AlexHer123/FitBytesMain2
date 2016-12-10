package com.example.alex.fitbytes;

import java.util.Calendar;


/**
 * Created by Ger on 11/27/2016.
 */

public class WeeklyGoal extends Goal {
    private final int WEEKLY_DURATION = 7;
    public WeeklyGoal(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        cal.set(year, month, day);

        Category c = new Exercise();
        setCategory(c);
        setDate(cal);
        setDueDate(getUpdatedDate());
        setType(Type.WEEKLY);
    }
    public WeeklyGoal(String description, int date, int dueDate){
        Category c = new Exercise(description);
        setCategory(c);
        setDate(date);
        setDueDate(dueDate);
        setType(Type.WEEKLY);
    }
    public String toString(){
        return "Weekly: " + getDescription() + (getCompleted()? " (Done)" : "");
    }
    private int getUpdatedDate(){
        Calendar c = Calendar.getInstance();
        int i = c.get(Calendar.WEEK_OF_MONTH);
        c.set(Calendar.WEEK_OF_MONTH, ++i);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String dateString = String.format("%04d%02d%02d", c.get(Calendar.YEAR),
                (c.get(Calendar.MONTH)), c.get(Calendar.DATE));
        return Integer.parseInt(dateString);
    }
}
