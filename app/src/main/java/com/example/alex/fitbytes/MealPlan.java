package com.example.alex.fitbytes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MealPlan extends MainActivity {

    private String[] monthList = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int currentMonth, currentDate, currentYear;
    private int selectedMonth, selectedDate, selectedYear, selectedRecipeID;
    private String selectedRecipeName, removeRecipeName;
    private int oldDate;
    private DBHandler db = new DBHandler(this);
    private boolean editMeal = false;
    private MealPlanItem mpItem = new MealPlanItem();
    private TextView noMeals;
    private Boolean flags[] = {false,false}; // Flags to see if we can add {date, recipeInfo}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        noMeals = (TextView)findViewById(R.id.textView_mp_meals);
        checkForMeals();

        editMeal = getIntent().getBooleanExtra("edit", false);
        if (!editMeal) {
            setEditVisibility(View.INVISIBLE);
        }
        else{
            mpItem = (MealPlanItem)getIntent().getSerializableExtra("mpItem");
            updateRecipeSpinner();
            oldDate = mpItem.getDate();
            setSelectedDate(oldDate);
            writeDateTextView(convertDates(mpItem));
            selectedRecipeID = mpItem.getRecipeID();
            selectedRecipeName = mpItem.getRecipeName();
            mpItem.printContents();
//            writeRecipeTextView(selectedRecipeName);
            setEditVisibility(View.VISIBLE);
        }

        setCurrentDate();

        // Set cancel button listener
        Button cancelButton = (Button)findViewById(R.id.button_mp_cancel);
        cancelButton.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealPlan.this, UpcomingPlans.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });



        // Set add button listener
        Button addButton = (Button)findViewById(R.id.button_mp_addMeal);
        addButton.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Add meal
                String message = "";
                int selectionDate = getSelectedDate();
                if (!editMeal && MealPlan.this.checkFlags()) {
                    boolean addAll = db.addAllMeals(mpItem);
                    // Displays a message
                    if (addAll){
                        Intent intent = new Intent(MealPlan.this, UpcomingPlans.class);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    else {
                        message = "Meal Plan Already Exists";
                        displayPopup(message);
                    }
                }
                else if (MealPlan.this.checkFlags()){
                    boolean updateMeal = db.updateMealPlan(oldDate, selectionDate, mpItem);
                    // Displays a message
                    if (updateMeal){
                        Intent intent = new Intent(MealPlan.this, UpcomingPlans.class);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    else {
                        message = "Edit not made";
                        displayPopup(message);
                    }
                }
                else{
                    message = "Invalid meal plan";
                    displayPopup(message);
                }
            }
        });

        // Set pick date button listener
        Button pickDateButton = (Button)findViewById(R.id.button_mp_pickDate);
        pickDateButton.setOnClickListener(new AdapterView.OnClickListener() {
            // On click, pass the current date and switch to CalendarPicker. Expect a date back.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealPlan.this, CalendarPicker.class);
                intent.putExtra("currentMonth", selectedMonth-1); // Make selectedMonth zero-based
                intent.putExtra("currentDate", selectedDate);
                intent.putExtra("currentYear", selectedYear);
                startActivityForResult(intent, 1);
            }
        });

        // Set add recipe button listener
        Button addRecipeButton = (Button)findViewById(R.id.button_mp_addRecipe);
        addRecipeButton.setOnClickListener(new AdapterView.OnClickListener(){
            // On click, switch to recipe. Expect a recipe ID back
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealPlan.this, Recipes.class);
                intent.putExtra("fromMP", true);
                startActivityForResult(intent, 2);
            }
        });

        // Set remove recipe button listener
        Button removeRecipeButton = (Button)findViewById(R.id.button_mp_removeRecipe);
        removeRecipeButton.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                mpItem.removeRecipe(removeRecipeName);
                updateRecipeSpinner();
            }
        });
    }

    // Used as a response to startActivityForResult() method. Determines what happens after a return from
    // another activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Handles return from CalendarPicker
        if (requestCode == 1) {
            // Set the selected date values
            if(resultCode == Activity.RESULT_OK){
                selectedMonth = intent.getIntExtra("month", currentMonth)+1;
                selectedDate = intent.getIntExtra("date", currentDate);
                selectedYear = intent.getIntExtra("year", currentYear);
                mpItem.setMpDate(selectedMonth, selectedDate, selectedYear);
                writeDateTextView(monthList[selectedMonth-1]+" " + selectedDate+ ", " + selectedYear);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        // Handles return from Recipes
        else if (requestCode == 2){
            // Set the selected date values
            if(resultCode == Activity.RESULT_OK){
                selectedRecipeID = intent.getIntExtra("recipeID", -1);
                selectedRecipeName = intent.getStringExtra("recipeName");
                mpItem.setRecipe(selectedRecipeID, selectedRecipeName);
                mpItem.addRecipe(selectedRecipeID,selectedRecipeName);
                updateRecipeSpinner();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void updateRecipeSpinner(){
        // Set up selected meals list view
        ListView selectedMeals = (ListView)findViewById(R.id.listView_mp_meals);
        Map<Integer, String> recipes = mpItem.getRecipes();
        List<String> recipeNames = new ArrayList<>(recipes.values());
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeNames);
        selectedMeals.setAdapter(myarrayAdapter);

        selectedMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                removeRecipeName = (String)parent.getItemAtPosition(position);
            }
        });

        checkForMeals();
    }

    // Creates the dropdowns
    private void setCurrentDate() {
        // Get date information
        final Calendar date = Calendar.getInstance();
        currentMonth = date.get(Calendar.MONTH); // zero-based
        currentDate = date.get(Calendar.DAY_OF_MONTH); // one-based
        currentYear = date.get(Calendar.YEAR);
    }

    // Used to add missing zeroes to date (ex: Jan 1, 2016 => 20160101)
    private int getSelectedDate(){
        String date = "" + selectedYear;
        if (selectedMonth < 10) date +="0";
        date += selectedMonth;
        if (selectedDate < 10) date += "0";
        date += selectedDate;
        return Integer.parseInt(date);
    }

    // Used with createMeals method to change database dates to correct format
    private String convertDates(MealPlanItem plan){
        String stringDate = "";
            String mpiDate = Integer.toString(plan.getDate());
            try {
                SimpleDateFormat MDYFormat = new SimpleDateFormat("yyyyMMdd");
                Date date = MDYFormat.parse(mpiDate);
                MDYFormat = new SimpleDateFormat("MMM d, yyyy");
                String thisDate = MDYFormat.format(date);
                stringDate = thisDate;
            } catch (ParseException e){ e.printStackTrace(); }
        return stringDate;
    }

    // Sets certain components visible/invisible when editing
    private void setEditVisibility(int visibility) {
        if (visibility == 0) editMeal = true;
        else editMeal = false;
        Button cancelButton = (Button)findViewById(R.id.button_mp_cancel);
        TextView editText = (TextView)findViewById(R.id.textView_mp_editing);
        editText.setVisibility(visibility);
    }

    // Writes the selected date on the screens TextView.
    private void writeDateTextView(String date){
        TextView dateTextView = (TextView)findViewById(R.id.textView_mp_date);
        if (date == null){
            date = "Pick a date";
        }
        else {
            setFlag(0, true);
        }
        dateTextView.setText(date);
    }

    private void setSelectedDate(int fullDate){
        selectedYear = fullDate/10000;
        selectedMonth = ((fullDate/100)%100); // Make it zero-based
        selectedDate = fullDate % 100;
    }

    private void checkForMeals() {
        if (mpItem.getRecipes().size() == 0) {
            noMeals.setVisibility(View.VISIBLE);
        }
        else{
            noMeals.setVisibility(View.INVISIBLE);
            setFlag(1, true);
        }
    }

    private void setFlag(int flagIndex, Boolean value){
        flags[flagIndex] = value;
    }

    private Boolean checkFlags(){
        for (Boolean b : flags){
            if (b == false) return false;
        }
        return true;
    }

    // Used whenever a popup happens
    private void displayPopup(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}