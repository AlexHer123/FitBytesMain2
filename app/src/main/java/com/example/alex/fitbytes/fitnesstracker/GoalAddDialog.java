package com.example.alex.fitbytes.fitnesstracker;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.alex.fitbytes.R;

/**
 * Created by ger on 11/16/16.
 */

public class GoalAddDialog extends DialogFragment {
    public View onCreateView(LayoutInflater inflater, Bundle savedInstanceState){
        return inflater.inflate(R.layout.goal_completion_dialog, null);
    }
}
