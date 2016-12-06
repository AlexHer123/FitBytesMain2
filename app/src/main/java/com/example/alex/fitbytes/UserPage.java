package com.example.alex.fitbytes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserPage extends MainActivity {

    private DBHandler db = new DBHandler(this);
    private MealPlanItem mpItem;
    private TextView noMeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        noMeals = (TextView)findViewById(R.id.textView_mp_meals);
        mpItem = db.getMealPlan(db.getCurrentDate());
        TextView dateView = (TextView)findViewById(R.id.textView_user_summary);
        dateView.setText(convertDate(mpItem.getDate()) + " Summary");
        updateRecipeSpinner();

        Button updateButton = (Button)findViewById(R.id.button_user_update);
        updateButton.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this, UserProfileActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Handles return from UserProfile
        if (requestCode == 1) {
            // Set the BMI values
            if (resultCode == Activity.RESULT_OK) {

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void updateRecipeSpinner(){
        // Set up selected meals list view
        ListView selectedMeals = (ListView)findViewById(R.id.listView_mp_meals);
//        Map<Integer, String> recipes = mpItem.getRecipes();
        List<String> recipeNames = new ArrayList<>(mpItem.getRecipes().values());
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeNames);
        selectedMeals.setAdapter(myarrayAdapter);

        selectedMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                removeRecipeName = (String)parent.getItemAtPosition(position);
            }
        });

        checkForMeals();
    }

    private void checkForMeals() {
        if (mpItem.getRecipes().size() == 0) {
            noMeals.setVisibility(View.VISIBLE);
        }
        else{
            noMeals.setVisibility(View.INVISIBLE);
        }
    }

    private String convertDate(int mealDate){
        String currentDate = Integer.toString(mealDate);
        try {
            SimpleDateFormat MDYFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = MDYFormat.parse(currentDate);
            MDYFormat = new SimpleDateFormat("MMM d");
            currentDate = MDYFormat.format(date);
//            stringDates.add(thisDate + " : " + p.getSize() + " Meal(s) planned.");
        } catch (ParseException e){ e.printStackTrace(); }
        return currentDate;
    }
}
