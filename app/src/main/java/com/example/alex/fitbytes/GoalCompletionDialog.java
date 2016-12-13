package com.example.alex.fitbytes;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


/**
 * Created by ger on 11/16/16.
 */

public class GoalCompletionDialog extends DialogFragment {
    public View onCreateView(LayoutInflater inflater, Bundle savedInstanceState){
        return inflater.inflate(R.layout.goal_completion_dialog, null);
    }
}

