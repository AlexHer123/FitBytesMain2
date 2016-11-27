package com.example.alex.fitbytes;

/**
 * Created by Ger on 11/27/2016.
 */

public class CustomCategory extends Category {
    public CustomCategory(){
        setOption(getDefault());
    }
    public CustomCategory(String option){
        super(option);
    }
    @Override
    protected String getDefault() {
        return "No defined goal set.";
    }
}
