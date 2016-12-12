package com.example.alex.fitbytes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class UserStatsActivity extends MainActivity {

    int lifetimeCalories, lifetimeFat, lifetimeCarbs, lifetimeSugar, lifetimeChol, lifetimeSodium, lifetimeProtein;
    int totalMeals;

    private DBHandler userDB = new DBHandler(this);
    private UserItem user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);

        user = userDB.getUser();
        getNutrientTotals();
        fillInfo();
    }

    private void getNutrientTotals(){
        totalMeals = user.getTotalMeals();
        lifetimeCalories = user.getCals();
        lifetimeFat = user.getFat();
        lifetimeCarbs = user.getCarbs();
        lifetimeSugar = user.getSugar();
        lifetimeChol = user.getChol();
        lifetimeSodium = user.getSodium();
        lifetimeProtein = user.getProtein();
    }

    public void fillInfo(){
        TextView lifeTimeCalText = (TextView)findViewById(R.id.userStatsCalTotal);
        lifeTimeCalText.setText("" + lifetimeCalories);

        TextView lifeFatText = (TextView)findViewById(R.id.userStatsFatTotal);
        lifeFatText.setText("" + lifetimeFat);

        TextView lifeCarbsText = (TextView)findViewById(R.id.userStatsCarbsTotal);
        lifeCarbsText.setText("" + lifetimeCarbs);

        TextView lifeSugarText = (TextView)findViewById(R.id.userStatsSugarTotal);
        lifeSugarText.setText("" + lifetimeSugar);

        TextView lifeCholText = (TextView)findViewById(R.id.userStatsCholTotal);
        lifeCholText.setText("" + lifetimeChol);

        TextView lifeSodiumText = (TextView)findViewById(R.id.userStatsSodiumTotal);
        lifeSodiumText.setText("" + lifetimeSodium);

        TextView lifeProteinText = (TextView)findViewById(R.id.userStatsProteinTotal);
        lifeProteinText.setText("" + lifetimeProtein);


        TextView mealCal = (TextView)findViewById(R.id.userStatCalPerMeal);
        mealCal.setText("" + perMeal(lifetimeCalories));

        TextView mealFat = (TextView)findViewById(R.id.userStatsFatPerMeal);
        mealFat.setText("" + perMeal(lifetimeFat));

        TextView mealCarbs = (TextView)findViewById(R.id.userStatsCarbsPerMeal);
        mealCarbs.setText("" + perMeal(lifetimeCarbs));

        TextView mealSugar = (TextView)findViewById(R.id.userStatsSugarPerMeal);
        mealSugar.setText("" + perMeal(lifetimeSugar));

        TextView mealChol = (TextView)findViewById(R.id.userStatsCholPerMeal);
        mealChol.setText("" + perMeal(lifetimeChol));

        TextView mealSodium = (TextView)findViewById(R.id.userStatsSodiumPerMeal);
        mealSodium.setText("" + perMeal(lifetimeSodium));

        TextView mealProtein = (TextView)findViewById(R.id.userStatsProteinPerMeal);
        mealProtein.setText("" + perMeal(lifetimeProtein));
}

    /**
     *
     * @param nutrient value, totalMeals must be populated first
     * @return
     */
    private int perMeal(int nutrient){
        if(totalMeals != 0)
            return nutrient / totalMeals;
        else
            return 0;
    }
}
