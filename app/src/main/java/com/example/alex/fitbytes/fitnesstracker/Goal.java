package com.example.alex.fitbytes.fitnesstracker;

import java.util.Calendar;

/**
 * Created by Alex on 10/21/2016.
 */

public abstract class Goal {
    protected String description;
    protected int date;
    protected int duration;
    protected boolean completed;
    protected Category category;

    public boolean getCompleted(){ return completed; }
    public void setCompleted(boolean b){ completed = b; }
    public String getDescription(){return description;}
    public int getDate(){return date;}
    public int getDuration(){return duration; }
    //public void setDescription(String description){this.description = description;}
    //public void setDate(int date){ this.date = date; }
    //public void setDuration(int duration){ this.duration = duration; }
}
