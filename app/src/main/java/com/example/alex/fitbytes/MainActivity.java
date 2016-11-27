package com.example.alex.fitbytes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Integer num = 0;
    private String[] recipes = {"PB&J", "Ramen", "Cereal", "Grilled Cheese", "Spaghetti"};
    private DBHandler db = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Creates a toolbar for MainActivity but throws the exception for other classes
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }catch (IllegalStateException e){}

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c.getTime());
        int date = Integer.parseInt(formattedDate);
//        db.removeOldStuff(db.getCurrentDate());
        db.addCurrentDate(date);
        storeAllRecipes();
    }

    private void storeAllRecipes(){
        for (String r: recipes){
            String formatRecipe = r.toLowerCase().replaceAll("\\s", "");
            boolean added = db.addRecipe(formatRecipe, r);
        }
    }

// THE UNDO MENU PROCESS
//    Uncomment button stuff
//    Uncomment xml in content_main
//    Change all main classes to extend AppCompatActivity
//    Remove overrides of onOptionsItemSelected in each class
//    Can leave menu stuff, but don't click on it in the app

//    public void mealPlanButtonOnClick(View view){
//        Intent intent = new Intent(this, MealPlan.class);
////        Intent intent = new Intent(this, UpcomingPlans.class); // DO THIS IN SPRINT 2
//        startActivity(intent);
//    }
//
//    public void fitnessTrackerButtonOnClick(View view) {
//        Intent intent = new Intent(this, FitnessTracker.class);
//        startActivity(intent);
//    }
//
//    public void ingredientButtonOnClick(View view) {
//        Intent intent = new Intent(this, Ingredients.class);
//        startActivity(intent);
//    }
//
//    public void recipeButtonOnClick(View view){
//        Intent intent = new Intent(this, Recipes.class);
//        startActivity(intent);
//    }
//
//    public void userProfileButtonOnClick(View view){
//        Intent intent = new Intent(this, UserProfileActivity.class);
//        startActivity(intent);
//    }

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
        if (id == R.id.action_userProfile) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_mealPlan) {
            Intent intent = new Intent(this, MealPlan.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_recipe) {
            Intent intent = new Intent(this, Recipes.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_pantry) {
            Intent intent = new Intent(this, Pantry.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_fitnessTracker) {
            Intent intent = new Intent(this, FitnessTracker.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            db.resetDatabase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
