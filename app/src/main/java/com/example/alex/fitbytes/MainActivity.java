package com.example.alex.fitbytes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    Integer num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void mealPlanButtonOnClick(View view){
        Intent intent = new Intent(this, MealPlan.class);
        startActivity(intent);
    }

    protected static boolean goalSet = false;
    protected static DefaultGoals goal1 = new DefaultGoals();
    protected static DefaultGoals goal2 = new DefaultGoals();
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
