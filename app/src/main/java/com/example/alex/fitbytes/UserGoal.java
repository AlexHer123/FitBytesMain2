package com.example.alex.fitbytes;

/**
 * Created by Ger on 11/27/2016.
 */

public class UserGoal extends Goal {
    public UserGoal(){
        Category c = new CustomCategory();
        setCategory(c);
        setDuration(0);
        setType(Type.USER);
    }
    public UserGoal(String description, int date, int duration){
        Category c = new CustomCategory(description);
        setCategory(c);
        setDuration(duration);
        setDate(date);
        setType(Type.USER);
    }
    public String getDescription() {
        /*if(super.getDescription().contains("User: "))
            return super.getDescription();
        else return "User: " + super.getDescription();*/
        return super.getDescription();
    }
}
