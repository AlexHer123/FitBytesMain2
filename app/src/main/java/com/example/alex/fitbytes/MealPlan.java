package com.example.alex.fitbytes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MealPlan extends AppCompatActivity {

    private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int currentMonth;
    private Integer numbers[] = new Integer[7];
    private String[] mealList = {"asdfsadfsadf", "dasfasdf", "asdfsadf","asdfsadfsadf", "dasfasdf", "asdfsadf","asdfsadfsadf", "dasfasdf", "asdfsadf"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        Calendar date = Calendar.getInstance();

//        SimpleDateFormat month_date = new SimpleDateFormat("MM");
//        String month_name = month_date.format(date.getTime());
        currentMonth = date.get(Calendar.MONTH); //NOTE: Calendar.MONTH is zero based

        for (int i = 0; i < 7; i++) {
            numbers[i] = date.get(Calendar.DAY_OF_MONTH);
            date.add(Calendar.DAY_OF_MONTH, 1);
        }

        createDropdowns();
        createMeals();
    }

    private void createDropdowns() {
        Spinner monthDropdown = (Spinner)findViewById(R.id.monthSpinner);
        ArrayAdapter<String> dayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, months);
        monthDropdown.setAdapter(dayAdapter);
        monthDropdown.setSelection(currentMonth);

        Spinner numDropdown = (Spinner)findViewById((R.id.daySpinner));
        ArrayAdapter<Integer> numAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, numbers);
        numDropdown.setAdapter(numAdapter);
    }

    private void createMeals(){
        ListView lv = (ListView)findViewById(R.id.plannedMeals);
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mealList);
        lv.setAdapter(myarrayAdapter);
    }
}
