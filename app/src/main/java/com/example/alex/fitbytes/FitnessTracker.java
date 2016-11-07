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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FitnessTracker extends MainActivity {
    private ArrayAdapter<String> goalAdapter;
    private DBHandler goalDB = new DBHandler(this);
    boolean isWeekly = false;

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
        getWeeklyGoals();
        if(!isWeekly) setGoalAdapter(1);
        else setGoalAdapter(7);
        updateGoalList();
        Button addButton = (Button) findViewById(R.id.addGoal);
        addButton.setOnClickListener(
                new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isWeekly) {
                            addGoal(createDailyGoal());
                            updateGoalList();
                            displayPopup("Daily Goal has been added.");
                        } else {
                            addGoal(createWeeklyGoal());
                            updateGoalList();
                            displayPopup("Weekly Goal has been added.");
                        }
                    }
                }
        );
        final Button changeButton = (Button) findViewById(R.id.toWeekly);
        changeButton.setOnClickListener(
                new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isWeekly) {
                            setGoalAdapter(7);
                            updateGoalList();
                            changeButton.setText("Go to Daily Goals");
                            TextView tv = (TextView) findViewById(R.id.textView6);
                            tv.setText("Weekly Goals");
                            isWeekly = true;
                        } else {
                            setGoalAdapter(1);
                            updateGoalList();
                            changeButton.setText("Go to Weekly Goals");
                            TextView tv = (TextView) findViewById(R.id.textView6);
                            tv.setText("Daily Goals");
                            isWeekly = false;
                        }
                    }
                }
        );
        //Button finishButton = (Button) findViewById(R.id.null);
        displayGoalAdapter((ListView) findViewById(R.id.daily_goals));
        goalDB.removeGoal(goalDB.getCurrentDate());
        goalDB.removeWeeklyGoal(goalDB.getCurrentDate());

    }
    private void setGoalAdapter(){
        List<String> goalList = new ArrayList<>();
        for(Goal goal : goalDB.getAllGoals()){
            if(goal.getCompleted()) {
                goalList.add(goal.getDescription() + " (Done)");
            } else {
                goalList.add(goal.getDescription());
            }
        }
        goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goalList);
    }
    private void setGoalAdapter(int i){
        List<String> goalList = new ArrayList<>();
        for(Goal goal : goalDB.getAllGoals(i)){
            if(goal.getCompleted()) {
                goalList.add(goal.getDescription() + " (Done)");
            } else {
                goalList.add(goal.getDescription());
            }
        }
        goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goalList);
    }
    private void displayGoalAdapter(ListView listView) {
        listView.setAdapter(goalAdapter);
    }
    private void getDailyGoals(){
        if(goalDB.getAllGoals(1).isEmpty()){
            Goal[] g = {createDailyGoal(), createDailyGoal(), createDailyGoal()};
            for(Goal goal : g){
                goalDB.addGoal(goal.getDescription(), goal.getDate(), goal.getDuration(), goal.getCompleted());
            }
        }
    }
    private Goal createDailyGoal(){
        Goal dailyGoal = new Goal(goalDB.getCurrentDate());
        dailyGoal.setDuration(1);
        return dailyGoal;
    }

    private void getWeeklyGoals(){
        if(goalDB.getAllGoals(7).isEmpty()){
            Goal[] g = {createWeeklyGoal(), createWeeklyGoal()};
            for(Goal goal : g){
                goalDB.addGoal(goal.getDescription(), goal.getDate(), goal.getDuration(), goal.getCompleted());
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
        goalDB.addGoal(goal.getDescription(), goal.getDate(), goal.getDuration(), goal.getCompleted());
        goalAdapter.add(goal.getDescription());
    }
    private void updateGoalList() {
        ListView goalsListView = (ListView) findViewById(R.id.daily_goals);
        attachGoalListener(goalsListView);
        goalsListView.setAdapter(goalAdapter);
    }
    private void attachGoalListener(ListView goalsListView){
        goalsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                        final int s = parent.getSelectedItemPosition();
                        final String e = parent.getItemAtPosition(position).toString();
                        int i = e.indexOf(" (Done)");
                        String j = e;
                        if(i != -1) {
                            j = j.substring(0, i);
                        }
                        final String selectedRow = j;
                        final Dialog dialog = new Dialog(FitnessTracker.this);
                        dialog.setTitle("Goal: " + selectedRow);
                        dialog.setContentView(R.layout.goal_dialog);
                        dialog.show();
                        if(!goalDB.getGoal(selectedRow).getCompleted()){
                            Button finishButton = (Button) dialog.findViewById(R.id.goal_mark_as_complete);
                            finishButton.setOnClickListener(
                                    new AdapterView.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //goalDB.removeGoal(20161107);
                                            //goalAdapter.remove(goalAdapter.getItem(s));
                                            goalDB.setGoalCompleted(selectedRow);
                                            dialog.dismiss();
                                            displayPopup("Goal has been completed");
                                            if(!isWeekly)
                                                setGoalAdapter(1);
                                            else setGoalAdapter(7);
                                            updateGoalList();
                                        }
                                    }
                            );
                        } else {
                            Button unfinishButton = (Button) dialog.findViewById(R.id.goal_mark_as_complete);
                            unfinishButton.setText("Mark as Incomplete");
                            unfinishButton.setOnClickListener(
                                    new AdapterView.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //goalDB.removeGoal(20161107);
                                            //goalAdapter.remove(goalAdapter.getItem(s));
                                            goalDB.setGoalIncompleted(selectedRow);
                                            dialog.dismiss();
                                            displayPopup("Goal is now incomplete");
                                            if(!isWeekly)
                                                setGoalAdapter(1);
                                            else setGoalAdapter(7);
                                            updateGoalList();
                                        }
                                    }
                            );
                        }
                    }
                }
        );
    }
}

