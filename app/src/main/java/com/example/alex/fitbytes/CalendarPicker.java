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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class CalendarPicker extends AppCompatActivity {

    private DBHandler db = new DBHandler(this);
    private int currentMonth, currentDate, currentYear;
    private int selectedMonth, selectedDate, selectedYear;
    private int tempMonth, tempDate, tempYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_calendar);

        // Get current date
        int todaysDate = db.getCurrentDate();
        currentYear = todaysDate/10000;
        currentMonth = ((todaysDate/100)%100)-1; // Make it zero-based
        currentDate = todaysDate % 100;

        // Set the temporarily selected date
        tempYear = currentYear;
        tempMonth = currentMonth;
        tempDate = currentDate;

        // Get the currently selected date from the previous class. Note, this will be used later to
        // set the calendar to the correct date.
        Intent thisIntent = getIntent();
        selectedMonth = thisIntent.getIntExtra("currentMonth", currentMonth);
        selectedDate = thisIntent.getIntExtra("currentDate", currentDate);
        selectedYear = thisIntent.getIntExtra("currentYear", currentYear);

        // Set the CalendarView listener
        CalendarView calendarView = (CalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            // Whenever a date is selected, set it as temporarily selected.
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                tempMonth = month;
                tempDate = dayOfMonth;
                tempYear = year;

                if (tempMonth <= currentMonth && tempDate < currentDate && tempYear == currentYear) {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "Cannot select date", duration);
                    toast.show();
                }
                else {
                    final Dialog dialog = new Dialog(CalendarPicker.this);
                    dialog.setTitle("Meal Plan for " + tempYear + tempMonth + tempDate);
                    dialog.setContentView(R.layout.activity_calendar_popup);
                    TextView dateText = (TextView) dialog.findViewById((R.id.textView_calpop_date));
                    dateText.setText("" + (tempMonth+1) + "/" + tempDate + "/" + tempYear);
                    int date = CalendarPicker.this.dateToInt(tempMonth+1, tempDate, tempYear);
                    MealPlanItem mpi = db.getMealPlan(date);
                    TextView mealListText = (TextView) dialog.findViewById(R.id.textView_calpop_mealList);
                    mealListText.setText("");
                    if (mpi != null){
                        for (String name : mpi.getRecipeNames())
                        mealListText.append(" - " + name + "\n\n");
                    }
                    else {
                        mealListText.setText("No meals to display");
                    }

                    // Set the select button listener
                    dialog.show();
                    Button selectButton = (Button) dialog.findViewById(R.id.calendarSelect_button);
                    selectButton.setOnClickListener(new AdapterView.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedMonth = tempMonth;
                            selectedDate = tempDate;
                            selectedYear = tempYear;
                            Intent intent = new Intent(CalendarPicker.this, MealPlan.class);
                            intent.putExtra("month", selectedMonth);
                            intent.putExtra("date", selectedDate);
                            intent.putExtra("year", selectedYear);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
//                        }
                        }
                    });

                }
            }
        });

        // Set cancel button listener
        Button cancelButton = (Button)findViewById(R.id.calendarCancel_button);
        cancelButton.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarPicker.this, MealPlan.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    // Used to combine the date pieces for database queries
    private int dateToInt(int... brokenDate) {
        String date = "" + brokenDate[2];
        if (brokenDate[0] < 10) date += "0";
        date += brokenDate[0];
        if (brokenDate[1] < 10) date +="0";
        date += brokenDate[1];
        return Integer.parseInt(date);
    }
}
