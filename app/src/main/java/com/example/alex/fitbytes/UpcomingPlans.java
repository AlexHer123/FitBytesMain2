package com.example.alex.fitbytes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class UpcomingPlans extends AppCompatActivity {

    private List<String> mealList;
    private DBHandler db = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_plans);

        mealList = db.getAllPlans();
        createMeals();
    }


    private void createMeals(){
        ListView lv = (ListView)findViewById(R.id.plannedMeals);
        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mealList);
        lv.setAdapter(myarrayAdapter);
    }
}
