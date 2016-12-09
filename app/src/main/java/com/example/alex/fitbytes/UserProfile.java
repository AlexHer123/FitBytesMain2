package com.example.alex.fitbytes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class UserProfile extends MainActivity {

    private DBHandler db = new DBHandler(this);
    private MealPlanItem mpItem;
    private TextView noMeals;
    private int currentDate;
    private UserItem user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        user = db.getUser();
        currentDate = db.getCurrentDate();
        noMeals = (TextView)findViewById(R.id.textView_mp_meals);
        mpItem = db.getMealPlan(currentDate);
        if (mpItem == null) {
            mpItem = new MealPlanItem();
        }
        fillOutInformation();
        TextView dateView = (TextView)findViewById(R.id.textView_user_summary);
        dateView.setText("Summary for: " + convertDate(currentDate));
        updateRecipeSpinner();

        Button updateButton = (Button)findViewById(R.id.button_user_update);
        updateButton.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, UserInfo.class);
                intent.putExtra("user", user);
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
                db.updateUser(intent.getStringExtra("name"), intent.getIntExtra("height", 0), intent.getIntExtra("weight", 0), intent.getDoubleExtra("BMI", 0));
//                user = new UserItem(intent.getStringExtra("name"), intent.getIntExtra("height", 0), intent.getIntExtra("weight", 0), intent.getDoubleExtra("BMI", 0));
                user = db.getUser();
                Log.d("USER: ", user.getName());
                fillOutInformation();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void fillOutInformation(){
        TextView username = (TextView)findViewById(R.id.textView_userPage_Hello);
        TextView height = (TextView)findViewById(R.id.textView_user_height);
        TextView weight = (TextView)findViewById(R.id.textView_user_weight);
        TextView BMI = (TextView)findViewById(R.id.textView_user_bmi);

        username.setText("Hello " + user.getName() + ",");
        int h = user.getHeight();
        String hi = (h/12) + " ft. " + (h%12) + " in.";
        height.setText("Height: " + hi);
        weight.setText("Weight: " + user.getWeight());
        BMI.setText("BMI: " + user.getBMI());
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
