package com.example.alex.fitbytes;

import java.util.Random;

/**
 * Created by ger on 11/19/16.
 */

public class Exercise extends Category{
    public static final String[] DEFAULT_OPTIONS = {
            "push ups",
            "sit ups",
            "jumping jacks",
            "squats",
            "pull ups",
            "jogging",
            "walking",
            "crunches"
    };
    public Exercise(){
        this.setOption(getDefault());
    }
    public Exercise(String option){
        super(option);
    }
    protected String getDefault(){
        String option = DEFAULT_OPTIONS[(int)(Math.random()*DEFAULT_OPTIONS.length-1)];
        String optionDescription;
        Random random = new Random();
        switch(option){
            case "walking":
                int minutes = 0;
                do{
                    minutes = random.nextInt(20);
                }while(!(minutes % 5 == 0 && minutes >= 10));
                optionDescription = String.format("Take a walk for %s minutes", minutes);
                break;
            case "jogging":
                do{
                    minutes = random.nextInt(20);
                }while(!(minutes % 5 == 0 && minutes >= 10));
                optionDescription = String.format("Jog for %s minutes", minutes);
                break;
            default:
                int reps = 0;
                do{
                    reps = random.nextInt(40);
                }while(!(reps % 5 == 0 && reps >= 20));
                optionDescription = String.format("Finish %s reps of %s", reps, option);
                break;
        }
        return optionDescription;
    }
}
