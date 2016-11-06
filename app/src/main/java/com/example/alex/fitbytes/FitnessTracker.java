package com.example.alex.fitbytes;

import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class FitnessTracker extends MainActivity {
    private ArrayAdapter<String> goalAdapter;
    private DBHandler goalDB = new DBHandler(this);

    private void displayPopup(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_tracker);
        //goals = new ArrayList<>();
        getDailyGoals();
        setGoalAdapter();
        Button addButton = (Button) findViewById(R.id.addGoal);
        addButton.setOnClickListener(
                new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Goal goal = new Goal();
                        addGoal(goal);
                        updateGoalList();
                        displayPopup("Goal has been added.");
                    }
                }
        );
        displayGoalAdapter((ListView) findViewById(R.id.daily_goals));
    }
    private void setGoalAdapter(){
        List<String> goalList = new ArrayList<>();
        for(Goal goal : goalDB.getAllGoals()){ goalList.add(goal.getDescription()); }
        goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goalList);
    }
    private void displayGoalAdapter(ListView listView) {
        listView.setAdapter(goalAdapter);
    }
    private void getDailyGoals(){
        if(goalDB.getAllGoals().isEmpty()){
            Goal[] g = {createDailyGoal(), createDailyGoal(), createDailyGoal()};
            for(Goal goal : g){
                goalDB.addGoal(goal.getDescription(), goal.getDate(), goal.getDuration());
            }
        }
    }
    private Goal createDailyGoal(){
        Goal dailyGoal = new Goal();
        dailyGoal.setDuration(1);
        return dailyGoal;
    }

    /*private void getWeeklyGoals(){
        if(goalDB.getAllGoals() == null){
            Goal[] g = {createWeeklyGoal(), createWeeklyGoal()};
            for(Goal goal : g){
                goalDB.addGoal(goal.getDescription(), goal.getDate(), goal.getDuration());
            }
        }
    }*/
    /*private Goal createWeeklyGoal(){
        Goal weeklyGoal = new Goal();
        weeklyGoal.setDuration(7);
        return weeklyGoal;
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_fitnessTracker || super.onOptionsItemSelected(item);
    }
    private void addGoal(Goal goal){
        goalDB.addGoal(goal.getDescription(), goal.getDate(), goal.getDuration());
        goalAdapter.add(goal.getDescription());
    }
    private void updateGoalList() {
        ListView goalsListView = (ListView) findViewById(R.id.daily_goals);
        goalsListView.setAdapter(goalAdapter);
    }
}

