package com.example.alex.fitbytes;

import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Integer num = 0;
    private DBHandler db = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c.getTime());
        int date = Integer.parseInt(formattedDate);
        db.addCurrentDate(date);

        List<Integer> dateList = db.getAllDates();
        for (int a : dateList){
            Log.d("HERE IT IS: ", Integer.toString(a));
        }
    }

    public void mealPlanButtonOnClick(View view){
        Intent intent = new Intent(this, MealPlan.class);
//        Intent intent = new Intent(this, UpcomingPlans.class); // DO THIS IN SPRINT 2
        startActivity(intent);
    }

    //protected static boolean goalSet = false;
    protected static Goal goal1 = new Goal();
    protected static Goal goal2 = new Goal();
    public void fitnessTrackerButtonOnClick(View view) {
        Intent intent = new Intent(this, FitnessTracker.class);
        startActivity(intent);
    }

    public void ingredientButtonOnClick(View view) {
        Intent intent = new Intent(this, Ingredients.class);
        startActivity(intent);
    }

    public void recipeButtonOnClick(View view){
        Intent intent = new Intent(this, Recipes.class);
        startActivity(intent);
    }

    public void userProfileButtonOnClick(View view){
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
