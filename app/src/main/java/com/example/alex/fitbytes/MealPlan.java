package com.example.alex.fitbytes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MealPlan extends MainActivity {

    private String[] monthList = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
//    private String[] recipes = {"PB&J", "Ramen", "BBQ Chicken", "Cereal", "Grilled Cheese", "Spaghetti & Meatballs", "Hamburger", "Oatmeal", "Nachos","Chicken Burrito"};
    private List<String> recipes = new ArrayList<>();
    private int currentMonth, currentDate, currentYear;
    private int selectedMonth, selectedDate, selectedYear, selectedRecipe;
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
                displayPopup("Edit cancelled");
            }
        });
        setEditVisibility(View.INVISIBLE);

        // Set add button listener
        Button addButton = (Button)findViewById(R.id.addMeal);
        addButton.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View v) {
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
//                    db.removePlan(oldDate);
                    boolean edited = db.updatePlan(oldDate, selectionDate, selectedRecipe);
                    setEditVisibility(View.INVISIBLE);
                    // Displays a message
                    if (edited){
                        message = "Edit made";
//                        db.removePlan(oldDate);
                    }
                    else
                        message = "Edit not made";
                }
                MealPlan.this.createMeals(convertDates(db.getAllPlans()));
                displayPopup(message);
            }
        });

        // Set pick date button listener
        Button pickDateButton = (Button)findViewById(R.id.pick_button);
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

        // Set pick recipe button listener
        Button pickRecipeButton = (Button)findViewById(R.id.button_mp_recipe);
        pickRecipeButton.setOnClickListener(new AdapterView.OnClickListener(){
            // On click, switch to recipe. Expect a recipe ID back
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealPlan.this, Recipes.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    // Used as a response to startActivityForResult() method. Determines what happens after a return from
    // another activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Handles return from CalendarPicker
        if (requestCode == 1) {
            Log.d("HKDJSHF", "kdshfdks");
            // Set the selected date values
            if(resultCode == Activity.RESULT_OK){
                selectedMonth = intent.getIntExtra("month", currentMonth)+1;
                selectedDate = intent.getIntExtra("date", currentDate);
                selectedYear = intent.getIntExtra("year", currentYear);
                writeDateTextView();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        // Handles return from Recipes
        else if (requestCode == 2){
            Log.d("Hasdfasdf", "kdshfdks");
        }
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
        currentDate = date.get(Calendar.DAY_OF_MONTH); // one-based
        currentYear = date.get(Calendar.YEAR);

        // Set selected date as current date. Writes it on the screen
        selectedMonth = currentMonth+1;
        selectedDate = currentDate;
        selectedYear = currentYear;
        writeDateTextView();
        // Create each individual dropdown
//        createCalendarDays(date);
//        createMonthSpinner(selectedMonth);
//        createDaySpinner(selectedDate-1);
        createRecipeSpinner(0);
    }

    // Create Month dropdown
//    private void createMonthSpinner(int defaultMonth) {
//        // Set up month dropdown
//        final Spinner monthDropdown = (Spinner)findViewById(R.id.monthSpinner);
//        ArrayAdapter<String> dayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, monthList);
//        monthDropdown.setAdapter(dayAdapter);
//        monthDropdown.setSelection(defaultMonth);
//
//        // Listener for month dropdown
//        monthDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedMonth = position+1;
//                Calendar newDate = new GregorianCalendar(Calendar.YEAR, position, 1);
//                createCalendarDays(newDate);
//                if (setDay) {
//                    createDaySpinner(0);
//                }
//                setDay = true;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // DO NOTHING
//            }
//        });
//    }

    // Create day dropdown
//    private void createDaySpinner(int defaultDay) {
//        // Set up date dropdown
//        Spinner numDropdown = (Spinner)findViewById((R.id.daySpinner));
//        ArrayAdapter<Integer> numAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, allDays);
//        numDropdown.setAdapter(numAdapter);
//        numDropdown.setSelection(defaultDay);
//
//        // Listener for date dropdown
//        numDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedDate = position+1;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // DO NOTHING
//            }
//        });
//    }

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
                        selectedDate = brokenDate[1];
                        selectedYear = brokenDate[2];
                        selectedRecipe = db.getMealRecipe(oldDate);
                        setDay = false;
                        writeDateTextView();
                        // Recreate dropdowns using selected date
//                        createMonthSpinner(selectedMonth-1);
//                        createDaySpinner(selectedDate-1);
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
//    private void createCalendarDays(Calendar date) {
//        int numDays = date.getActualMaximum(Calendar.DAY_OF_MONTH);
//        allDays = new ArrayList<Integer>();
//        for (int i = 1; i <= numDays; i++){
//            allDays.add(i);
//        }
//    }

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
        if (selectedDate < 10) date += "0";
        date += selectedDate;
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

    // Writes the selected date on the screens TextView.
    private void writeDateTextView(){
        TextView dateTextView = (TextView)findViewById(R.id.textView_mp_date);
        dateTextView.setText(monthList[selectedMonth-1]+" " + selectedDate+ ", " + selectedYear);
    }
}