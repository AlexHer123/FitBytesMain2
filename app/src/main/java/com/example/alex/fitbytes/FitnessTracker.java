package com.example.alex.fitbytes;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FitnessTracker extends MainActivity {
    private ArrayAdapter<Goal> goalAdapter;
    private DBHandler goalDB = new DBHandler(this);
    private Goal currentGoal;
    private ListView goalsListView;

    NotificationCompat.Builder notification;
    private static final int uniqueID = 10101;

    private void displayToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_tracker);
        goalsListView = (ListView) findViewById(R.id.current_goals);
        getDefaultGoals();
        updateGoalAdapter();
        //Button addButton = buildButton(findViewById(R.id.addGoal));
        FloatingActionButton floatingAddButton = buildFloatButton(findViewById(R.id.addGoalFloating));
        displayAdapter((ListView) findViewById(R.id.current_goals));
        goalDB.removeGoals();

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
    }

    private void toNotify(View view){
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker("Set Ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Set Content Title");
        notification.setContentText("Set Context Text");

        Intent intent = new Intent(this, FitnessTracker.class);
        PendingIntent pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pending);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(uniqueID, notification.build());
    }

    private FloatingActionButton buildFloatButton(View view){
        FloatingActionButton floatButton = (FloatingActionButton) view;
        floatButton.setOnClickListener(buildOnClickListener(view));
        return floatButton;
    }
    private Button buildButton(View view){
        Button button = (Button) view;
        button.setOnClickListener(buildOnClickListener(view));
        return button;
    }
    private View.OnClickListener buildOnClickListener(View view){
        View.OnClickListener listener = null;
        switch(view.getId()){
            /*case R.id.addGoal:
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
                                String olddateString = String.format("%04d%02d%02d", calendar.get(Calendar.YEAR),
                                        (calendar.get(Calendar.MONTH)), calendar.get(Calendar.DATE));
                                int day = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth();
                                int year =  datePicker.getYear();
                                calendar.set(year, month, day);
                                String dateString = String.format("%04d%02d%02d", calendar.get(Calendar.YEAR),
                                        (calendar.get(Calendar.MONTH) ), calendar.get(Calendar.DATE));
                                Goal userGoal = new UserGoal(goalDescription, Integer.parseInt(olddateString), Integer.parseInt(dateString));
                                goalDB.addGoal(userGoal);
                                updateGoalAdapter();
                                dialog.dismiss();
                                displayToast("Goal has been created");
                                toNotify(v);
                            }
                        });
                    }
                };
                break;*/
            case R.id.addGoalFloating:
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
                                String olddateString = String.format("%04d%02d%02d", calendar.get(Calendar.YEAR),
                                        (calendar.get(Calendar.MONTH)), calendar.get(Calendar.DATE));
                                int day = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth();
                                int year =  datePicker.getYear();
                                calendar.set(year, month, day);
                                String dateString = String.format("%04d%02d%02d", calendar.get(Calendar.YEAR),
                                        (calendar.get(Calendar.MONTH) ), calendar.get(Calendar.DATE));
                                Goal userGoal = new UserGoal(goalDescription, Integer.parseInt(olddateString), Integer.parseInt(dateString));
                                goalDB.addGoal(userGoal);
                                updateGoalAdapter();
                                dialog.dismiss();
                                displayToast("Goal has been created");
                                toNotify(v);
                            }
                        });
                    }
                };
                break;
            default:
                displayToast("" + "view.getId() = " + view.getId());
        }
        return listener;
    }
    private void updateGoalAdapter() {
        setGoalAdapter();
        updateGoalList();
    }
    private void setGoalAdapter(){
        List<Goal> goalList = new ArrayList<>();
        for(Goal goal : goalDB.getAllGoals()){
            if(goal.getCompleted()) {
                goalList.add(goal);
            } else {
                goalList.add(goal);
            }
        }
        goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goalList);
    }
    private void displayAdapter(ListView listView) {
        listView.setAdapter(goalAdapter);
    }
    private void getDefaultGoals(){
        if(goalDB.getAllGoals().isEmpty()){
            Goal[] g = {new DailyGoal(), new WeeklyGoal()};
            for(Goal goal : g) goalDB.addGoal(goal);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_fitnessTracker || super.onOptionsItemSelected(item);
    }
    private void updateGoalList() {
        goalsListView = (ListView) findViewById(R.id.current_goals);
        attachGoalListener(goalsListView);
        goalsListView.setAdapter(goalAdapter);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = info.position;
        currentGoal = (Goal) goalAdapter.getItem(position);
        final Dialog dialog = new Dialog(FitnessTracker.this, android.R.style.Theme_DeviceDefault_DialogWhenLarge);
        switch(item.getItemId()) {
            case R.id.edit:
                dialog.setTitle("Edit your goal and deadline");
                dialog.setContentView(R.layout.goal_add_goal_dialog);
                dialog.show();
                final EditText editText = (EditText) dialog.findViewById(R.id.editText);
                final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                editText.setText(currentGoal.getDescription());
                datePicker.setMinDate(System.currentTimeMillis() - 1000);
                Button cancelButton = (Button) dialog.findViewById(R.id.goal_add_goal_cancel);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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
                        String olddateString = String.format("%04s%02s%02s", calendar.get(Calendar.YEAR),
                                (calendar.get(Calendar.MONTH)), calendar.get(Calendar.DATE));
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth();
                        int year =  datePicker.getYear();
                        calendar.set(year, month, day);
                        String dateString = String.format("%04s%02s%02s", calendar.get(Calendar.YEAR),
                                (calendar.get(Calendar.MONTH)), calendar.get(Calendar.DATE));
                        Goal userGoal = new UserGoal(goalDescription, Integer.parseInt(olddateString), Integer.parseInt(dateString));
                        goalDB.updateGoal(userGoal, currentGoal);
                        updateGoalAdapter();
                        dialog.dismiss();
                        displayToast("Goal has been updated");
                    }
                });
                return true;
            case R.id.delete:
                displayToast("Goal has been deleted");
                goalDB.removeGoal(currentGoal);
                updateGoalAdapter();
                return true;
            case R.id.details:
                dialog.setTitle("Details");
                dialog.setContentView(R.layout.goal_completion_dialog);
                dialog.show();
                TextView currentGoalDescription = (TextView) dialog.findViewById(R.id.goal_completion_goal_description);
                TextView currentGoalDate = (TextView) dialog.findViewById(R.id.goal_completion_goal_date);
                TextView currentGoalDeadline = (TextView) dialog.findViewById(R.id.goal_completion_goal_deadline);
                currentGoalDescription.setText(currentGoal.getDescription());
                currentGoalDate.setText(currentGoal.getDateAsString(currentGoal.getDate()));
                currentGoalDeadline.setText(currentGoal.getDateAsString(currentGoal.getDueDate()));
                Button completionButton = (Button) dialog.findViewById(R.id.goal_mark_as_complete);
                cancelButton = (Button) dialog.findViewById(R.id.goal_go_back);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if(!currentGoal.getCompleted()){
                    completionButton.setOnClickListener(
                            new AdapterView.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    goalDB.setGoalCompleted(currentGoal, true);
                                    currentGoal.setCompleted(true);
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
                                    goalDB.setGoalCompleted(currentGoal, false);
                                    currentGoal.setCompleted(false);
                                    dialog.dismiss();
                                    displayToast("Goal is now incomplete");
                                    updateGoalAdapter();
                                }
                            }
                    );
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void attachGoalListener(ListView goalsListView){
        goalsListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
                if (view.getId() == R.id.current_goals) {
                    MenuInflater inflater = getMenuInflater();
                    inflater.inflate(R.menu.goal_menu_list, menu);
                }
            }

        });
        goalsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                        currentGoal = (Goal) parent.getItemAtPosition(position);
                        if(!currentGoal.getCompleted()){
                            goalDB.setGoalCompleted(currentGoal, true);
                            currentGoal.setCompleted(true);
                        } else {
                            goalDB.setGoalCompleted(currentGoal, false);
                            currentGoal.setCompleted(false);
                        }
                        updateGoalAdapter();

                    }
                }
        );
    }
}

