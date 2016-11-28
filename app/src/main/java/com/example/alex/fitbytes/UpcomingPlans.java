package com.example.alex.fitbytes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class UpcomingPlans extends AppCompatActivity {

    private List<String> mealList;
    private DBHandler db = new DBHandler(this);
    private int selectedMonth, selectedDate, selectedYear, selectedRecipeID;
    private int oldDate;
    private String selectedRecipeName;
    private boolean setDay = false;
    private List<MealPlanItem> mpItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_plans);

        mpItems = db.getAllPlans();
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
                mpItems = db.getAllPlans();
                createMeals(convertDates(mpItems));
                displayPopup("Plan added.");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                displayPopup("Cancelled add");
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
                final String rowRecipe = breakRow[1];
                final int[] brokenDate = separateDate(rowDate);

                // Create dialog popup
                final Dialog dialog = new Dialog(UpcomingPlans.this);
                dialog.setTitle("Meal Plan for " + rowDate);
                dialog.setContentView(R.layout.activity_meal_popup);
                TextView dateText = (TextView)dialog.findViewById(R.id.textView_mpPop_date);
                dateText.setText("Meal Plan: " + rowDate);
                TextView mealText =  (TextView)dialog.findViewById(R.id.mp_meal_text);
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
                        selectedRecipeName = mpItems.get(position).getRecipeName();
                        setDay = false;
                        dialog.dismiss();

                        Intent intent = new Intent(UpcomingPlans.this, MealPlan.class);
//                        intent.putExtra("oldDate", oldDate);
//                        intent.putExtra("selectedMonth", selectedMonth);
//                        intent.putExtra("selectedDate", selectedDate);
//                        intent.putExtra("selectedYear", selectedYear);
                        intent.putExtra("mpItem", mpItems.get(position));
                        intent.putExtra("edit", true);

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

    // Used with createMeals method to change database dates to correct format
    private List<String> convertDates(List<MealPlanItem> plans){
        List<String> stringDates = new ArrayList<String>();
        for (MealPlanItem p : plans){
            String mpiDate = Integer.toString(p.getDate());
            try {
                SimpleDateFormat MDYFormat = new SimpleDateFormat("yyyyMMdd");
                Date date = MDYFormat.parse(mpiDate);
                MDYFormat = new SimpleDateFormat("MMM d, yyyy");
                String thisDate = MDYFormat.format(date);
                stringDates.add(thisDate + " : " + p.getRecipeName());
            } catch (ParseException e){ e.printStackTrace(); }
        }
        return stringDates;
    }

    // Used whenever a popup happens
    private void displayPopup(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
