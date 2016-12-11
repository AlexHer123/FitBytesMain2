package com.example.alex.fitbytes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.Map;

public class UserStatsActivity extends AppCompatActivity {

    // TODO change names
    double lifetimeCalories, lifetimeCarbs, lifetimeSugar, lifetimeFat;

    int totalMeals;

    private DBHandler mealDB = new DBHandler(this);

    List<MealPlanItem> allMeals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);

        allMeals = mealDB.getAllMealPlans();

        //TODO calculate total nutritional stats from recipes, and total number of meals
        if(allMeals.size()>0){
            for(MealPlanItem mealPlan: allMeals){
                Map<Integer, String> currentRecipes = mealPlan.getRecipes();

            }
        }



    }
}
