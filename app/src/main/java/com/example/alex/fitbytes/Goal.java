package com.example.alex.fitbytes;

/**
 * Created by Alex on 10/21/2016.
 */

public class Goal {
    private final String[] exercise = {"push ups", "sit ups", "jumping jacks", "squats", "pullups"};
    //private int[] weight = {20, 20, 20, 20, 20};
    private String goal;
    private boolean isChecked;

    public Goal(){
        //goal = makeGoal();
        this("Nothing yet");
    }
    public Goal(String goal){
        //goal = makeGoal();
        this.goal = goal;
        isChecked = false;
    }
    public String getGoal(){
        return goal;
    }
    public boolean getStatus(){
        return isChecked;
    }
    public void setStatus(boolean status){
        isChecked = status;
    }
    public void setGoal(String goal){
        this.goal = goal;
    }
    private String makeGoal(){
        int rand = (int) (Math.random()*4);
        return "Do 10 " + exercise[rand];
    }
}
