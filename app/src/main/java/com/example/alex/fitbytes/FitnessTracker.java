package com.example.alex.fitbytes;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FitnessTracker extends MainActivity {
    private ArrayList<Goal> goals;
    private DBHandler goalsDatabase = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_tracker);
        //if(goals == null) goals = new ArrayList<>();
        Button addButton = (Button) findViewById(R.id.addGoal);
        addButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             goals.add(new DefaultGoal());
                                             //goalsDatabase.
                                             //createGoals();
                                             CharSequence text = "A new goal has added.";
                                             Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                                             toast.show();
                                         }
                                     }

        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_fitnessTracker) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createGoals(){
        ListView goalsListView = (ListView)findViewById(R.id.daily_goals);
        ArrayList<String> goalie = new ArrayList<>();
        for(Goal goal : goals){
            goalie.add(goal.getDescription());
        }
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goalie);
        goalsListView.setAdapter(myArrayAdapter);

        /*goalsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                final String selectedRow = parent.getItemAtPosition(position).toString();
                final Dialog dialog = new Dialog(FitnessTracker.this);
                //dialog.setTitle();
                dialog.setContentView(R.layout.activity_meal_popup);
                *//*TextView mealText = (TextView)dialog.findViewById(R.id.mp_meal_text);
                mealText.setText(thisMeal);*//*
                dialog.show();

                Button editButton = (Button)dialog.findViewById((R.id.mp_button_edit));
                editButton.setOnClickListener(new AdapterView.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                // Set up listener for delete button
                Button deleteButton = (Button)dialog.findViewById(R.id.mp__button_delete);
                deleteButton.setOnClickListener(new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //db.removePlan(thisDate);
                        createGoals();
                        dialog.dismiss();

                        // Displays a message
                        Context context = getApplicationContext();
                        CharSequence text = "Goal Removed";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
            }
            });*/
        }
        }

