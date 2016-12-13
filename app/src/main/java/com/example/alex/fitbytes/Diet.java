package com.example.alex.fitbytes;

/**
 * Created by ger on 11/19/16.
 */

public class Diet extends Category {
    public static final String[] DEFAULT_OPTIONS = {
            "meal plan",
            "calories"
            //"weight"
    };
    public Diet(){
        this.setOption(getDefault());
    }
    public Diet(String option){
        super(option);
    }
    protected String getDefault(){
        String option = DEFAULT_OPTIONS[(int)(Math.random()*DEFAULT_OPTIONS.length-1)];
        String optionDescription;
        switch(option){
            case "meal plan":
                optionDescription = String.format("Create a %s", option);
                break;
            case "calories":
                optionDescription = String.format("Consume your target amount of %s", option);
                break;
            /*case "weight":
                optionDescription = String.format("%s", option);
                break;*/
            default:
                optionDescription = "Do " + (int)(Math.random()*30+10)+ " " + option;
                break;
        }
        return optionDescription;
    }
}
