package com.example.alex.fitbytes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class UserStatsActivity extends AppCompatActivity {

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
        TextView lifeTimeCal = (TextView)findViewById(R.id.userStatsCalTotal);
     //   lifeTimeCal.setText("" + lifeTimeCal);
        Toast.makeText( getApplicationContext(), "" + lifeTimeCal, Toast.LENGTH_LONG).show();
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
            return -1;
    }
}
