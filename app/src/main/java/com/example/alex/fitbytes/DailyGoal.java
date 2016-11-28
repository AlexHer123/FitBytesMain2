package com.example.alex.fitbytes;

import java.util.Calendar;

/**
 * Created by Ger on 11/27/2016.
 */

public class DailyGoal extends Goal {
    private final int DAILY_DURATION = 1;
    public DailyGoal(int currentDate){
        Category c = new Exercise();
        setCategory(c);
        setDuration(currentDate);
        setDate(getUpdatedDate());
        setType(Type.DAILY);
    }
    public DailyGoal(String description, int date, int duration){
        Category c = new Exercise(description);
        setCategory(c);
        setDate(date);
        setDuration(duration);
        setType(Type.DAILY);
    }
    @Override
    public String getDescription() {
        if(super.getDescription().contains("Daily: "))
            return super.getDescription();
        else return "Daily: " + super.getDescription();
    }
    private int getUpdatedDate(){
        Calendar c = Calendar.getInstance();
        String dateString = String.format("%s%s%s", c.get(Calendar.YEAR),
                (c.get(Calendar.MONTH)+1), c.get(Calendar.DATE));
        return Integer.parseInt(dateString);
    }
}
