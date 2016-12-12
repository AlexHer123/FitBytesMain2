package com.example.alex.fitbytes;

/**
 * Created by Ger on 11/27/2016.
 */

public class UserGoal extends Goal {
    public UserGoal(){
        Category c = new CustomCategory();
        setCategory(c);
        setDueDate(getDate());
        setType(Type.USER);
    }
    public UserGoal(String description, int date, int duration){
        Category c = new CustomCategory(description);
        setCategory(c);
        setDueDate(duration);
        setDate(date);
        setType(Type.USER);
    }
    public String getDescription() {
        return super.getDescription();
    }

}
