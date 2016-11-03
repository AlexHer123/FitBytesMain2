package com.example.alex.fitbytes;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MealPlan extends AppCompatActivity {

    private String[] monthList = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private Integer weekNumbers[] = new Integer[7];
    private String[] recipes = {"Steak", "Salmon", "Baked Chicken"};
    private List<String> mealList;
    private String month;
    private int currentMonth;
    private String date;
    private String recipe;
    private DBHandler db = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        Calendar date = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        month = month_date.format(date.getTime());
        currentMonth = date.get(Calendar.MONTH); //NOTE: Calendar.MONTH is zero based

        for (int i = 0; i < 7; i++) {
            weekNumbers[i] = date.get(Calendar.DAY_OF_MONTH);
            date.add(Calendar.DAY_OF_MONTH, 1);
        }

        createDropdowns();
        createMeals();

        // Set add button listener
        Button addButton = (Button)findViewById(R.id.addMeal);
        addButton.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View v) {
//                Calendar currentDate = Calendar.getInstance();
//                String date =


                String plan = makeMealPlan();
                //mealList.add(new mealContainer(plan, recipe));
                boolean added = db.addPlan(plan, recipe);
                MealPlan.this.createMeals();

                // Displays a message
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                CharSequence text;
                if (added){
                    text = "Meal Plan Added";
                }
                else {
                    text = "Meal Plan Already Exists";
                }
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }

    private void createDropdowns() {
        // Set up month dropdown
        final Spinner monthDropdown = (Spinner)findViewById(R.id.monthSpinner);
        ArrayAdapter<String> dayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, monthList);
        monthDropdown.setAdapter(dayAdapter);
        monthDropdown.setSelection(currentMonth);

        // Listener for month dropdown
        monthDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MealPlan.this.setMonth(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // DO NOTHING
            }
        });

        // Set up date dropdown
        Spinner numDropdown = (Spinner)findViewById((R.id.daySpinner));
        ArrayAdapter<Integer> numAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, weekNumbers);
        numDropdown.setAdapter(numAdapter);

        // Listener for date dropdown
        numDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MealPlan.this.setDate(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // DO NOTHING
            }
        });

        Spinner recipeDropdown = (Spinner)findViewById(R.id.recipeSpinner);
        ArrayAdapter<String> recipeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, recipes);
        recipeDropdown.setAdapter(recipeAdapter);

        // Listener for date dropdown
        recipeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MealPlan.this.setRecipe(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // DO NOTHING
            }
        });

    }

    private void createMeals(){
        // Set up Upcoming meals list view
        mealList = db.getAllPlans();
        ListView upcomingMeals = (ListView)findViewById(R.id.plannedMeals);
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mealList);
        upcomingMeals.setAdapter(myarrayAdapter);

        // Set up listener for Upcoming meal items
        upcomingMeals.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create dialog box to display date and meals
                final String selectedRow = parent.getItemAtPosition(position).toString();
                int spaceIndex = selectedRow.indexOf(" ", 5);
                final String thisDate = selectedRow.substring(0, spaceIndex);
                final String thisMeal = selectedRow.substring(spaceIndex+1);
                final Dialog dialog = new Dialog(MealPlan.this);
                dialog.setTitle("Meal Plan for " + thisDate);
                dialog.setContentView(R.layout.activity_meal_popup);
                TextView mealText = (TextView)dialog.findViewById(R.id.mp_meal_text);
                mealText.setText(thisMeal);
                dialog.show();

                Button editButton = (Button)dialog.findViewById((R.id.mp_button_edit));
                editButton.setOnClickListener(new AdapterView.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                // Set up listener for delete button
                Button deleteButton = (Button)dialog.findViewById(R.id.mp__button_delete);
                deleteButton.setOnClickListener(new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.removePlan(thisDate);
                        createMeals();
                        dialog.dismiss();

                        // Displays a message
                        Context context = getApplicationContext();
                        CharSequence text = "Meal Plan Removed";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
            }
        });
    }

    private void setMonth(String m){
        month = m;
    }

    private void setDate(String d){
        date = d;
    }

    private void setRecipe(String r){
        recipe = r;
    }

    private String makeMealPlan(){
        return month + " " + date;
    }
}
