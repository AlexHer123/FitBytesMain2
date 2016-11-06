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
    private ArrayList<Goal> goals;
    private int selectedDate;
    private String selectedGoal;
    private DBHandler goalDB = new DBHandler(this);
    private boolean addState;
    private boolean editState;
    private boolean deleteState;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void displayPopup(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
    /*private Goal addGoalDialog(){
        DialogFragment addGoalDialog = new DialogFragment();
        return null;
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_tracker);
        goals = new ArrayList<>();
        getFakeDailyGoals();
        setGoalAdapter();
        //getDailyGoals();
        //getWeeklyGoals();
        //goals = (ArrayList<Goal>) goalDB.getAllGoals();
        Button addButton = (Button) findViewById(R.id.addGoal);
        addButton.setOnClickListener(
                new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Goal goal = new Goal();
                        addGoal(goal);
                        updateGoalList();
                        //goalAdapter.add(goal.getDescription());
                        /*boolean added = goalDB.addGoal(goal.getDescription(), goal.getDate());*/
                        displayPopup("Goal has been added.");
                    }
                }
        );
        displayGoalAdapter((ListView) findViewById(R.id.daily_goals));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void setGoalAdapter(){
        List<String> goalList = new ArrayList<>();
        for(Goal goal : goals){ goalList.add(goal.getDescription()); }
        goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goalList);
    }
    private void displayGoalAdapter(ListView listView) {
        listView.setAdapter(goalAdapter);
    }

    private void getFakeDailyGoals(){
        if(goals.isEmpty()){
            goals.add(new Goal());
            goals.add(new Goal());
            goals.add(new Goal());
        }
    }

    private void getDailyGoals(){
        if(goalDB.getAllGoals() == null){
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

    private void getWeeklyGoals(){
        if(goalDB.getAllGoals() == null){
            Goal[] g = {createWeeklyGoal(), createWeeklyGoal()};
            for(Goal goal : g){
                goalDB.addGoal(goal.getDescription(), goal.getDate(), goal.getDuration());
            }
        }
    }
    private Goal createWeeklyGoal(){
        Goal weeklyGoal = new Goal();
        weeklyGoal.setDuration(7);
        return weeklyGoal;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_fitnessTracker || super.onOptionsItemSelected(item);
    }
    private void addGoal(Goal goal){
        //goalDB.addGoal(goal.getDescription(), goal.getDate(), goal.getDuration());
        goals.add(goal);
        goalAdapter.add(goal.getDescription());
    }
    private void updateGoalList() {
        ListView goalsListView = (ListView) findViewById(R.id.daily_goals);
        goalsListView.setAdapter(goalAdapter);
    }
/*
    *//**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     *//*
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("FitnessTracker Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }*/
}

