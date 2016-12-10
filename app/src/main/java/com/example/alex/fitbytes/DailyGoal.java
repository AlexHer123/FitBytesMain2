package com.example.alex.fitbytes;

import java.util.Calendar;

/**
 * Created by Ger on 11/27/2016.
 */

public class DailyGoal extends Goal {
    private final int DAILY_DURATION = 1;
    public DailyGoal(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        cal.set(year, month, day);

        Category c = new Exercise();
        setCategory(c);
        setDate(cal);
        setDueDate(getUpdatedDate());
        setType(Type.DAILY);
    }
    public DailyGoal(String description, int date, int dueDate){
        Category c = new Exercise(description);
        setCategory(c);
        setDate(date);
        setDueDate(dueDate);
        setType(Type.DAILY);
    }
    public String toString(){
        return "Daily: " + getDescription() + (getCompleted()? " (Done)" : "");
    }
    private int getUpdatedDate(){
        Calendar c = Calendar.getInstance();
        String dateString = String.format("%04d%02d%02d", c.get(Calendar.YEAR),
                (c.get(Calendar.MONTH)), c.get(Calendar.DATE));
        return Integer.parseInt(dateString);
    }
}
