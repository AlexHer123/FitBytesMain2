package com.example.alex.fitbytes;

/**
 * Created by Alex on 10/21/2016.
 */

public class DefaultGoals {
    final String[] exercise = {"push ups", "sit ups", "jumping jacks", "squats", "pullups"};
    int[] weight = {20, 20, 20, 20, 20};

    private String goal;
    public DefaultGoals()
    {
        goal = makeGoal();
    }

    public String getGoal(){
        return goal;
    }

    private String makeGoal(){
        int rand = (int) (Math.random()*4);
        return "Do 10 " + exercise[rand];
    }
}
