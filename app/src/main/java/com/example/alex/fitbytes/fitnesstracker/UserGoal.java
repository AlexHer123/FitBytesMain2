package com.example.alex.fitbytes.fitnesstracker;

/**
 * Created by ger on 11/19/16.
 */

public class UserGoal extends Goal{
    public UserGoal(){

    }
    public UserGoal(String description, int date, int duration){
        this.description = description;
        this.date = date;
        this.duration = duration;
    }
}
