package com.example.alex.fitbytes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpcomingPlans extends MainActivity {

    private List<String> mealList;
    private DBHandler db = new DBHandler(this);
    private int selectedMonth, selectedDate, selectedYear, selectedRecipeID;
    private int oldDate;
    private String selectedRecipeName;
    private boolean setDay = false;
    private List<MealPlanItem> mpItems = new ArrayList<>();
    private TextView noPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_plans);

        noPlans = (TextView)findViewById(R.id.textView_up_meals);
//        mpItems = db.getAllPlans();
        mpItems = db.getAllMealPlans();
        createMeals(convertDates(mpItems));

        Button addButton = (Button)findViewById(R.id.uplanAdd_button);
        addButton.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpcomingPlans.this, MealPlan.class);
                intent.putExtra("edit", false);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Handles return from MealPlan after adding
        if (requestCode == 1) {
            // Set the selected date values
            if(resultCode == Activity.RESULT_OK){
//                mpItems = db.getAllPlans();
//                createMeals(convertDates(mpItems));

                mpItems = db.getAllMealPlans();
                createMeals(convertDates(mpItems));
                displayPopup("Plan added.");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                displayPopup("Cancelled add.");
            }
        }
        // Handles return from MealPlan after edit
        else if (requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
                mpItems = db.getAllMealPlans();
                createMeals(convertDates(mpItems));
                displayPopup("Edit made.");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                displayPopup("Cancelled edit.");
            }
        }
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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // Get date and meal from selected row
                final String selectedRow = parent.getItemAtPosition(position).toString();
                String[] breakRow = selectedRow.split(" : ");
                final String rowDate = breakRow[0];
//                final String rowRecipe = breakRow[1];
//                final int[] brokenDate = separateDate(rowDate);

                final List<String> recipeNames = mpItems.get(position).getRecipeNames();
                final int[] brokenDate = separateDate(mpItems.get(position).getDate());

                // Create dialog popup
                final Dialog dialog = new Dialog(UpcomingPlans.this);
                dialog.setTitle("Meal Plan for " + rowDate);
                dialog.setContentView(R.layout.activity_meal_popup);
                TextView dateText = (TextView)dialog.findViewById(R.id.textView_mpPop_date);
                dateText.setText(rowDate);
                TextView mealText =  (TextView)dialog.findViewById(R.id.mp_meal_text);
                mealText.setText("");
                for (String n : recipeNames){
                    mealText.append(" - " + n + "\n\n");
                }
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
//                        selectedRecipeName = mpItems.get(position).getRecipeName();
                        setDay = false;
                        dialog.dismiss();

                        Intent intent = new Intent(UpcomingPlans.this, MealPlan.class);
                        intent.putExtra("mpItem", mpItems.get(position));
                        intent.putExtra("edit", true);
                        startActivityForResult(intent, 2);


                    }
                });

                // Set up listener for delete button
                Button deleteButton = (Button)dialog.findViewById(R.id.mp__button_delete);
                deleteButton.setOnClickListener(new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.removeMealPlan(dateArrayToInt(brokenDate));
                        mpItems = db.getAllMealPlans();
                        createMeals(convertDates(mpItems));
                        dialog.dismiss();

                        // Displays a message
                        displayPopup("Meal Plan Removed");
                    }
                });
            }
        });

        checkForPlans();
    }

    private int[] separateDate(int thisDate) {
        int[] brokenDate = new int[3];
        int month, day, year;

        // Get each piece of the date
        SimpleDateFormat editButtonFormat = new SimpleDateFormat("yyyyMMdd");
        try{
            Date date = editButtonFormat.parse(""+thisDate);
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

    // Used with createMeals method to change database dates to correct format
    private List<String> convertDates(List<MealPlanItem> plans){
        List<String> stringDates = new ArrayList<>();
        for (MealPlanItem p : plans){
            String mpiDate = Integer.toString(p.getDate());
            try {
                SimpleDateFormat MDYFormat = new SimpleDateFormat("yyyyMMdd");
                Date date = MDYFormat.parse(mpiDate);
                MDYFormat = new SimpleDateFormat("MMM d, yyyy");
                String thisDate = MDYFormat.format(date);
                stringDates.add(thisDate + " : " + p.getSize() + " Meal(s) planned.");
            } catch (ParseException e){ e.printStackTrace(); }
        }
        return stringDates;
    }

    private void checkForPlans() {
        if (mpItems.size() == 0) {
            noPlans.setVisibility(View.VISIBLE);
        }
        else{
            noPlans.setVisibility(View.INVISIBLE);
        }
    }

    // Used whenever a popup happens
    private void displayPopup(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}