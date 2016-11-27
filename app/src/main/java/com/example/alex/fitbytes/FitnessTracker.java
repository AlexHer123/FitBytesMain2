package com.example.alex.fitbytes;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FitnessTracker extends MainActivity {
    public static final String GOAL_DONE_MESSAGE = " (Done)";
    private ArrayAdapter<String> goalAdapter;
    private DBHandler goalDB = new DBHandler(this);

    private void displayToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_tracker);
        getDefaultGoals();
        updateGoalAdapter();
        Button addButton = buildButton(findViewById(R.id.addGoal));
        displayGoalAdapter((ListView) findViewById(R.id.current_goals));
        goalDB.removeDailyGoal(goalDB.getCurrentDate());
        //goalDB.removeWeeklyGoal(goalDB.getCurrentDate());

    }
    private Button buildButton(View view){
        Button button = (Button) view;
        button.setOnClickListener(buildOnClickListener(view));
        return button;
    }
    private View.OnClickListener buildOnClickListener(View view){
        View.OnClickListener listener = null;
        switch(view.getId()){
            case R.id.addGoal:
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*final Dialog dialog = new Dialog(FitnessTracker.this, android.R.style.Theme_DeviceDefault_DialogWhenLarge);
                        dialog.setTitle("What would you like to do?");
                        dialog.setContentView(R.layout.goal_add_goal_dialog);
                        dialog.show();*//*
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        displayToast(""+goalDB.getCurrentDate() + " " + c.getTime());*/
                    }
                };
                break;
            /*case R.id.toWeekly:
                listener = new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        *//*if (!isWeekly) {
                            changeButton.setText("Go to Daily Goals");
                            TextView tv = (TextView) findViewById(R.id.textView6);
                            tv.setText("Weekly Goals");
                            isWeekly = true;
                        } else {
                            changeButton.setText("Go to Weekly Goals");
                            TextView tv = (TextView) findViewById(R.id.textView6);
                            tv.setText("Daily Goals");
                            isWeekly = false;
                        }*//*
                        //updateGoalAdapter();
                        //nothing for now//
                    }
                };
                break;*/
            default:
                displayToast("" + "view.getId() = " + view.getId() + " and R.id.addGoal = " + R.id.addGoal);
        }
        return listener;
    }
    private void updateGoalAdapter() {
        setGoalAdapter();
        updateGoalList();
    }
    private void setGoalAdapter(){
        List<String> goalList = new ArrayList<>();
        for(Goal goal : goalDB.getAllGoals()){
            if(goal.getCompleted()) {
                goalList.add(goal.getDescription() + GOAL_DONE_MESSAGE);
            } else {
                goalList.add(goal.getDescription());
            }
        }
        goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goalList);
    }
    private void displayGoalAdapter(ListView listView) {
        listView.setAdapter(goalAdapter);
    }
    private void getDefaultGoals(){
        if(goalDB.getAllGoals().isEmpty()){
            Goal[] g = {new DailyGoal(), new WeeklyGoal()};
            for(Goal goal : g){
                goalDB.addGoal(goal.getDescription(), goal.getDate(), goal.getDuration(), goal.getCompleted(), goal.getType());
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_fitnessTracker || super.onOptionsItemSelected(item);
    }
/*    private void addGoalToGoalAdapter(Goal goal){
        goalDB.addGoal(goal.getDescription(), goal.getDate(), goal.getDuration(), goal.getCompleted());
        goalAdapter.add(goal.getDescription());
    }*/
    private void updateGoalList() {
        ListView goalsListView = (ListView) findViewById(R.id.current_goals);
        attachGoalListener(goalsListView);
        goalsListView.setAdapter(goalAdapter);
    }
    private void attachGoalListener(ListView goalsListView){
        goalsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                        final String sel = parent.getItemAtPosition(position).toString();
                        final int index = sel.indexOf(GOAL_DONE_MESSAGE);
                        final String selectedRow = index != -1 ? sel.substring(0, index) : sel;
                        final Dialog dialog = new Dialog(FitnessTracker.this, android.R.style.Theme_DeviceDefault_InputMethod);
                        dialog.setTitle("Goal: " + selectedRow);
                        dialog.setContentView(R.layout.goal_completion_dialog);
                        dialog.show();
                        Button completionButton = (Button) dialog.findViewById(R.id.goal_mark_as_complete);
                        if(!goalDB.getGoal(selectedRow).getCompleted()){
                            completionButton.setOnClickListener(
                                    new AdapterView.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            goalDB.setGoalCompleted(selectedRow);
                                            dialog.dismiss();
                                            displayToast("Goal has been completed");
                                            updateGoalAdapter();
                                        }
                                    }
                            );
                        } else {
                            completionButton.setText("Mark as Incomplete");
                            completionButton.setOnClickListener(
                                    new AdapterView.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            goalDB.setGoalIncompleted(selectedRow);
                                            dialog.dismiss();
                                            displayToast("Goal is now incomplete");
                                            updateGoalAdapter();
                                        }
                                    }
                            );
                        }
                    }
                }
        );
    }
}

