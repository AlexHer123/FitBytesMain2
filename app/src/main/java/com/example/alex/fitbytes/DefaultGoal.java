package com.example.alex.fitbytes;

/**
 * Created by ger on 11/2/16.
 */

public class DefaultGoal extends Goal {
    private final String[] DEFAULT = {"push ups", "sit ups", "jumping jacks", "squats", "pullups"};
    public DefaultGoal(){
        setDescription(initDefaultGoal());
    }
    private String initDefaultGoal(){
        return String.format("%s %s %s",
                "Do",
                (int)(Math.random()*30)+1,
                DEFAULT[(int)(Math.random()*DEFAULT.length-1)]);
        //"Do "+ (int)(Math.random()*30) + DEFAULT[(int) (Math.random()*(DEFAULT.length-1))];
    }
}
