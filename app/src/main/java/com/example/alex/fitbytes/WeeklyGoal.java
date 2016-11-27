package com.example.alex.fitbytes;

import java.util.Calendar;


/**
 * Created by Ger on 11/27/2016.
 */

public class WeeklyGoal extends Goal {
    private final int WEEKLY_DURATION = 7;
    public WeeklyGoal(){
        Category c = new Exercise();
        setCategory(c);
        setDuration(WEEKLY_DURATION);
        setDate(getUpdatedDate());
        setType(Type.WEEKLY);
    }
    public WeeklyGoal(String description, int date, int duration){
        Category c = new Exercise(description);
        setCategory(c);
        setDate(date);
        setDuration(duration);
        setType(Type.WEEKLY);
    }
    @Override
    public String getDescription() {
        if(super.getDescription().contains("Weekly: "))
            return super.getDescription();
        else return "Weekly: " + super.getDescription();
    }
    private int getUpdatedDate(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String dateString = String.format("%s%s%s", c.get(Calendar.YEAR),
                (c.get(Calendar.MONTH)+1), c.get(Calendar.DATE));
        return Integer.parseInt(dateString);
    }
}
