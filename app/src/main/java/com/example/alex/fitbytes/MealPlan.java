package com.example.alex.fitbytes;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MealPlan extends MainActivity {

    private String[] monthList = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private List<Integer> allDays = new ArrayList<Integer>();
//    private String[] recipes = {"PB&J", "Ramen", "BBQ Chicken", "Cereal", "Grilled Cheese", "Spaghetti & Meatballs", "Hamburger", "Oatmeal", "Nachos","Chicken Burrito"};
    private List<String> recipes = new ArrayList<>();
    private int currentMonth, currentDay, currentYear;
    private int selectedMonth, selectedDay, selectedYear, selectedRecipe;
    private int oldDate;
    private int editVisible = View.INVISIBLE;
    private DBHandler db = new DBHandler(this);
    private boolean setDay = false;
    private boolean editMeal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        createDropdowns();
        createMeals(convertDates(db.getAllPlans()));

        // Set cancel button listener
        Button cancelButton = (Button)findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View v) {
                setEditVisibility(View.INVISIBLE);
                displayPopup("Edit canceled");
            }
        });
        setEditVisibility(View.INVISIBLE);

        // Set add button listener
        Button addButton = (Button)findViewById(R.id.addMeal);
        addButton.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Check what year to use
                if (selectedMonth-1 < currentMonth)
                    selectedYear = currentYear + 1;
                else if (selectedMonth-1 == currentMonth && selectedDay < currentDay)
                    selectedYear = currentYear + 1;
                else
                    selectedYear = currentYear;

                // Add meal
                String message = "";
                int selectionDate = getSelectedDate();

                if (!editMeal) {
                    boolean added = db.addPlan(selectionDate, selectedRecipe);
                    // Displays a message
                    if (added)
                        message = "Meal Plan Added";
                    else
                        message = "Meal Plan Already Exists";
                }
                else{
                    db.removePlan(oldDate);
                    boolean edited = db.addPlan(selectionDate, selectedRecipe);
                    setEditVisibility(View.INVISIBLE);
                    // Displays a message
                    if (edited)
                        message = "Edit made";
                    else
                        message = "Edit not made";
                }
                MealPlan.this.createMeals(convertDates(db.getAllPlans()));
                displayPopup(message);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_mealPlan) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Creates the dropdowns
    private void createDropdowns() {
        // Get date information
        final Calendar date = Calendar.getInstance();
        currentMonth = date.get(Calendar.MONTH); // zero-based
        currentDay = date.get(Calendar.DAY_OF_MONTH); // one-based
        currentYear = date.get(Calendar.YEAR);

        // Create each individual dropdown
        createCalendarDays(date);
        createMonthSpinner(currentMonth);
        createDaySpinner(currentDay-1);
        createRecipeSpinner(0);
    }

    // Create Month dropdown
    private void createMonthSpinner(int defaultMonth) {
        // Set up month dropdown
        final Spinner monthDropdown = (Spinner)findViewById(R.id.monthSpinner);
        ArrayAdapter<String> dayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, monthList);
        monthDropdown.setAdapter(dayAdapter);
        monthDropdown.setSelection(defaultMonth);

        // Listener for month dropdown
        monthDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position+1;
                Calendar newDate = new GregorianCalendar(Calendar.YEAR, position, 1);
                createCalendarDays(newDate);
                if (setDay) {
                    createDaySpinner(0);
                }
                setDay = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // DO NOTHING
            }
        });
    }

    // Create day dropdown
    private void createDaySpinner(int defaultDay) {
        // Set up date dropdown
        Spinner numDropdown = (Spinner)findViewById((R.id.daySpinner));
        ArrayAdapter<Integer> numAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, allDays);
        numDropdown.setAdapter(numAdapter);
        numDropdown.setSelection(defaultDay);

        // Listener for date dropdown
        numDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // DO NOTHING
            }
        });
    }

    // Create Recipe dropdown
    private void createRecipeSpinner(int defaultRecipe) {
        recipes = db.getAllRecipes();
        Spinner recipeDropdown = (Spinner)findViewById(R.id.recipeSpinner);
        ArrayAdapter<String> recipeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, recipes);
        recipeDropdown.setAdapter(recipeAdapter);
        recipeDropdown.setSelection(defaultRecipe);

        // Listener for date dropdown
        recipeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRecipe = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // DO NOTHING
            }
        });
    }

    // Create upcoming meals list
    private void createMeals(List<String> meals){
        // Set up Upcoming meals list view
        ListView upcomingMeals = (ListView)findViewById(R.id.plannedMeals);
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, meals);
        upcomingMeals.setAdapter(myarrayAdapter);

        // Set up listener for Upcoming meal items
        upcomingMeals.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setEditVisibility(View.INVISIBLE);
                // Get date and meal from selected row
                final String selectedRow = parent.getItemAtPosition(position).toString();
                String[] breakRow = selectedRow.split(" : ");
                final String rowDate = breakRow[0];
                final String rowRecipe = breakRow[1];
                final int[] brokenDate = separateDate(rowDate);

                // Create dialog popup
                final Dialog dialog = new Dialog(MealPlan.this);
                dialog.setTitle("Meal Plan for " + rowDate);
                dialog.setContentView(R.layout.activity_meal_popup);
                TextView mealText = (TextView)dialog.findViewById(R.id.mp_meal_text);
                mealText.setText(rowRecipe);
                dialog.show();

                // Set up listener for edit button
                Button editButton = (Button)dialog.findViewById((R.id.mp_button_edit));
                editButton.setOnClickListener(new AdapterView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // Save old date and set it as the selected date
                        oldDate = dateArrayToInt(brokenDate);
                        selectedMonth = brokenDate[0];
                        selectedDay = brokenDate[1];
                        selectedRecipe = db.getMealRecipe(oldDate);
                        setDay = false;
                        // Recreate dropdowns using selected date
                        createMonthSpinner(selectedMonth-1);
                        createDaySpinner(selectedDay-1);
                        createRecipeSpinner(selectedRecipe);
                        setEditVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                });

                // Set up listener for delete button
                Button deleteButton = (Button)dialog.findViewById(R.id.mp__button_delete);
                deleteButton.setOnClickListener(new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.removePlan(dateArrayToInt(brokenDate));
                        createMeals(convertDates(db.getAllPlans()));
                        dialog.dismiss();

                        // Displays a message
                        displayPopup("Meal Plan Removed");
                    }
                });
            }
        });
    }

    // Used to get the number of days in a month
    private void createCalendarDays(Calendar date) {
        int numDays = date.getActualMaximum(Calendar.DAY_OF_MONTH);
        allDays = new ArrayList<Integer>();
        for (int i = 1; i <= numDays; i++){
            allDays.add(i);
        }
    }

    // Used by upcoming meal list to get date pieces
    private int[] separateDate(String thisDate) {
        int[] brokenDate = new int[3];
        int month, day, year;

        // Get each piece of the date
        SimpleDateFormat editButtonFormat = new SimpleDateFormat("MMM dd, yyyy");
        try{
            Date date = editButtonFormat.parse(thisDate);
            editButtonFormat = new SimpleDateFormat("MM");
            month = Integer.parseInt(editButtonFormat.format(date));
            editButtonFormat = new SimpleDateFormat("d");
            day = Integer.parseInt(editButtonFormat.format(date));
            editButtonFormat = new SimpleDateFormat("yyyy");
            year = Integer.parseInt(editButtonFormat.format(date));
            brokenDate[0] = month;
            brokenDate[1] = day;
            brokenDate[2] = year;
            return brokenDate;

        } catch (ParseException e){e.printStackTrace();}

        return null;
    }

    // Used to combine the date pieces for database queries
    private int dateArrayToInt(int[] brokenDate) {
        String date = "" + brokenDate[2];
        if (brokenDate[0] < 10) date += "0";
        date += brokenDate[0];
        if (brokenDate[1] < 10) date +="0";
        date += brokenDate[1];
        return Integer.parseInt(date);
    }

    // Used to add missing zeroes to date (ex: Jan 1, 2016 => 20160101)
    private int getSelectedDate(){
        String date = "" + selectedYear;
        if (selectedMonth < 10) date +="0";
        date += selectedMonth;
        if (selectedDay < 10) date += "0";
        date += selectedDay;
        return Integer.parseInt(date);
    }

    // Used with createMeals method to change database dates to correct format
    private List<String> convertDates(List<String> plans){
        List<String> stringDates = new ArrayList<String>();
        for (String p : plans){
            String[] breakPlans = p.split(" ");
            int mealIndex = Integer.parseInt(breakPlans[1]);
            // Format date to correct String and add it plus meal to list
            try {
                SimpleDateFormat MDYFormat = new SimpleDateFormat("yyyyMMdd");
                Date date = MDYFormat.parse(breakPlans[0]);
                MDYFormat = new SimpleDateFormat("MMM d, yyyy");
                String thisDate = MDYFormat.format(date);
                stringDates.add(thisDate + " : " + recipes.get(mealIndex));
            } catch (ParseException e){ e.printStackTrace(); }
        }
        return stringDates;
    }

    // Sets certain components visible/invisible when editing
    private void setEditVisibility(int visibility) {
        if (visibility == 0) editMeal = true;
        else editMeal = false;
        Log.d("editMeal value: ", Boolean.toString(editMeal));
        Button cancelButton = (Button)findViewById(R.id.button_cancel);
        TextView editText = (TextView)findViewById(R.id.tv_editing);
        cancelButton.setVisibility(visibility);
        editText.setVisibility(visibility);
    }

    // Used whenever a popup happens
    private void displayPopup(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}