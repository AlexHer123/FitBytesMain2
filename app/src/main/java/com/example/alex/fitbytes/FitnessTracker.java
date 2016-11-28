package com.example.alex.fitbytes;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        goalDB.removeWeeklyGoal(goalDB.getCurrentDate());
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
                        final Dialog dialog = new Dialog(FitnessTracker.this, android.R.style.Theme_DeviceDefault_DialogWhenLarge);
                        dialog.setTitle("Set your goal and deadline");
                        dialog.setContentView(R.layout.goal_add_goal_dialog);
                        dialog.show();
                        final EditText editText = (EditText) dialog.findViewById(R.id.editText);
                        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                        datePicker.setMinDate(System.currentTimeMillis() - 1000);
                        Button cancelButton = (Button) dialog.findViewById(R.id.goal_add_goal_cancel);
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //displayToast("Goal creation has been canceled");
                            }
                        });
                        Button finishButton = (Button) dialog.findViewById(R.id.goal_add_goal_finish);
                        finishButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String goalDescription = editText.getText().toString().trim();
                                if(goalDescription.isEmpty()){
                                    displayToast("Goal has no description");
                                    return;
                                }
                                Calendar calendar = Calendar.getInstance();
                                String olddateString = String.format("%s%s%s", calendar.get(Calendar.YEAR),
                                        (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.DATE));
                                int day = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth();
                                int year =  datePicker.getYear();
                                calendar.set(year, month, day);
                                String dateString = String.format("%s%s%s", calendar.get(Calendar.YEAR),
                                        (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.DATE));
                                Goal userGoal = new UserGoal(goalDescription, Integer.parseInt(olddateString), Integer.parseInt(dateString));
                                goalDB.addGoal(userGoal.getDescription(), userGoal.getDate(), userGoal.getDuration(), userGoal.getCompleted(), userGoal.getType());
                                updateGoalAdapter();
                                dialog.dismiss();
                                displayToast("Goal has been created");
                                //displayToast(olddateString + " " + dateString);
                            }
                        });
                    }
                };
                break;
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
            int currentDate = goalDB.getCurrentDate();
            Log.d("DSLFJSDLFJ", ""+currentDate);
            Goal[] g = {new DailyGoal(currentDate), new WeeklyGoal()};
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
                        final Dialog dialog = new Dialog(FitnessTracker.this, android.R.style.Theme_DeviceDefault_DialogWhenLarge);
                        dialog.setTitle("Details");
                        dialog.setContentView(R.layout.goal_completion_dialog);
                        dialog.show();

                        //TabHost t = (TabHost) findViewById(R.id.tab_host);
                        /*Tab tab1;
                        Tab tab2;
                        Tab tab3;*/


                        //EditText currentGoalDescription = (EditText) dialog.findViewById(R.id.goal_completion_goal_description);
                        Goal currentGoal = goalDB.getGoal(selectedRow);
                        TextView currentGoalDescription = (TextView) dialog.findViewById(R.id.goal_completion_goal_description);
                        TextView currentGoalDeadline = (TextView) dialog.findViewById(R.id.goal_completion_goal_deadline);
                        currentGoalDescription.setText(currentGoal.getDescription());
                        displayToast(""+currentGoal.getDuration());
                        currentGoalDeadline.setText(""+convertDate(currentGoal.getDuration()));
                        Button completionButton = (Button) dialog.findViewById(R.id.goal_mark_as_complete);
                        Button cancelButton = (Button) dialog.findViewById(R.id.goal_go_back);
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
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

    private String convertDate(int date){
            String mpiDate = Integer.toString(date);
            try {
                SimpleDateFormat MDYFormat = new SimpleDateFormat("yyyyMMdd");
                Date newDate = MDYFormat.parse(mpiDate);
                MDYFormat = new SimpleDateFormat("MMM d, yyyy");
                mpiDate = MDYFormat.format(newDate);
                return mpiDate;
            } catch (ParseException e){ e.printStackTrace(); }
        return "No date available";
    }
}