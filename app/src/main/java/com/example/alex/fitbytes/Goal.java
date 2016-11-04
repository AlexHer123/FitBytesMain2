package com.example.alex.fitbytes;

/**
 * Created by Alex on 10/21/2016.
 */

public abstract class Goal {
    //private final String[] exercise = {"push ups", "sit ups", "jumping jacks", "squats", "pullups"};
    //private int[] weight = {20, 20, 20, 20, 20};
    private String description;
    //private boolean isChecked;

    public Goal(){
        //description = makeGoal();
        this("Nothing yet");
    }
    public Goal(String description){
        //description = makeGoal();
        this.description = description;
        //isChecked = false;
    }
    public String getDescription(){return description;}
    //public boolean isChecked(){return isChecked;}
    /*public void setStatus(boolean status){
        isChecked = status;
    }*/
    public void setDescription(String description){
        this.description = description;
    }
    /*private String makeGoal(){
        int rand = (int) (Math.random()*4);
        return "Do 10 " + exercise[rand];
    }*/
}
