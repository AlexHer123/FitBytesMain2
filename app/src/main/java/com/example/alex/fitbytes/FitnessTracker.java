package com.example.alex.fitbytes;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.view.View;

public class FitnessTracker extends AppCompatActivity {

    private DefaultGoals goal1;
    private DefaultGoals goal2;
    private boolean check1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_tracker);
        setGoals();
        ((CheckBox) findViewById(R.id.dailyGoal1)).setText(goal1.getGoal());
        ((CheckBox) findViewById(R.id.dailyGoal2)).setText(goal2.getGoal());
    }

    public void goal1Checked(View view){
        CheckBox checkbox = (CheckBox) findViewById(R.id.dailyGoal1);
        if (check1) {
            checkbox.setText(goal1.getGoal());
            check1 = false;
        }
        else{
            checkbox.setText(goal1.getGoal() + " (DONE)");
            check1 = true;
        }
    }

    private void setGoals() {
        goal1 = MainActivity.goal1;
        goal2 = MainActivity.goal2;
    }
}
