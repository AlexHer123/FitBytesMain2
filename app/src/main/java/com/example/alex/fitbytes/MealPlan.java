package com.example.alex.fitbytes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MealPlan extends AppCompatActivity {

    private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int currentMonth;
    private Integer numbers[] = new Integer[7];
    private List<String> mealList;
    private String month;
    private String date;
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
            numbers[i] = date.get(Calendar.DAY_OF_MONTH);
            date.add(Calendar.DAY_OF_MONTH, 1);
        }
//        db.removeAll(); // USED TEMPORARILY TO CLEAR DATABASE
        mealList = db.getAllPlans();

        createDropdowns();
        createMeals();

        Button addButton = (Button)findViewById(R.id.addMeal);
        addButton.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View v) {
                String plan = makeMealPlan();
                mealList.add(plan);
                MealPlan.this.createMeals();
                db.addPlan(plan);
                // Reading all shops
                Log.d("Reading: ", "Reading all shops..");
                List<String> allDates = db.getAllPlans();

                for (String tempDate : allDates) {
                    Log.d("Date: ", tempDate);
                }

                // Displays a message
                Context context = getApplicationContext();
                CharSequence text = "Meal Plan Added";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }

    private void createDropdowns() {
        Spinner monthDropdown = (Spinner)findViewById(R.id.monthSpinner);
        ArrayAdapter<String> dayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, months);
        monthDropdown.setAdapter(dayAdapter);
        monthDropdown.setSelection(currentMonth);

        // Listener for month dropdown
        monthDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MealPlan.this.setMonth(parent.getSelectedItem().toString());
//                Log.d("GET ID", Long.toString(id)); // Maybe use this to make a Date type
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner numDropdown = (Spinner)findViewById((R.id.daySpinner));
        ArrayAdapter<Integer> numAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, numbers);
        numDropdown.setAdapter(numAdapter);
        // Listener for date dropdown
        numDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MealPlan.this.setDate(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createMeals(){
        ListView lv = (ListView)findViewById(R.id.plannedMeals);
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mealList);
        lv.setAdapter(myarrayAdapter);
    }

    public void setMonth(String m){
        month = m;
    }

    public String getMonth(){
        return month;
    }

    public void setDate(String d){
        date = d;
    }

    public String getDate(){
        return date;
    }

    public String makeMealPlan(){
        return month + " " + date;
    }
}
