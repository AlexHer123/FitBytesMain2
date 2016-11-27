package com.example.alex.fitbytes;

/**
 * Created by ger on 11/19/16.
 */

public abstract class Category {
    private String option;
    public Category(){};
    public Category(String option){
        this.option = option;
    }
    public void setOption(String option){
        this.option = option;
    }
    public String getOption(){
        return option;
    }
    protected abstract String getDefault();
}
