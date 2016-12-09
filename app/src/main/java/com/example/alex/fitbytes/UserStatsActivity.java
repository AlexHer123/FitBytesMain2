package com.example.alex.fitbytes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.Map;

public class UserStatsActivity extends AppCompatActivity {

    int lifetimeClories, lifetimeCarbs, lifetimeSugar, lifetimeFat;
    private DBHandler mealDB = new DBHandler(this);

    List<MealPlanItem> allMeals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);

        allMeals = mealDB.getAllMealPlans();

        if(allMeals.size()>0){
            for(MealPlanItem mealPlan: allMeals){
                Map<Integer, String> currentRecipes = mealPlan.getRecipes();

            }
        }



    }
}
