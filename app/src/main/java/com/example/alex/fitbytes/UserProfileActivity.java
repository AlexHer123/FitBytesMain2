package com.example.alex.fitbytes;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;


public class UserProfileActivity extends MainActivity {

    double height, weight;
    double BMI;
    boolean isHealthyBMI;

    int healthyBMImin = 19;
    int healthyBMImax = 25;
    int BMIconversionFactor = 703;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final EditText heightEditText = (EditText)findViewById(R.id.userProfileUserHeight);
        final EditText weightEditText = (EditText)findViewById(R.id.userProfileUserWeight);
        final TextView isBMIhealthyTextView= (TextView)findViewById(R.id.userProfileIsBMIhealthy);
        final TextView BMItextView = (TextView)findViewById(R.id.userProfileUserBMI);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("userProfilePref", MODE_PRIVATE);
        final SharedPreferences.Editor prefEditor = preferences.edit();

//        prefEditor.clear();
//        prefEditor.commit();

        // load height and weight from sharedPreferences with default values 70 and 170
        height = Double.parseDouble(preferences.getString("HEIGHT", "70"));
        weight = Double.parseDouble(preferences.getString("WEIGHT", "170"));

        //height = 70;
        //weight = 170;

        calculateBMI();

        heightEditText.setText(""+height);
        weightEditText.setText(""+weight);
        BMItextView.setText(BMItoString());

        isBMIhealthyTextView.setText(toHealthyBMIstring());

        TextWatcher heightTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    height = Double.parseDouble(charSequence.toString());

                    // convert height to string and store in shared preferences
                    prefEditor.putString("HEIGHT", Double.toString(height));
                    prefEditor.commit();

                    calculateBMI();
                    BMItextView.setText(BMItoString());
                    isBMIhealthyTextView.setText(toHealthyBMIstring());
                }
                //clear BMI fields when editText is empty
                catch(IllegalArgumentException e){
                    BMItextView.setText("");
                    BMI = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
        heightEditText.addTextChangedListener(heightTextWatcher);


        TextWatcher weightTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    weight = Double.parseDouble(charSequence.toString());

                    // convert weight to string and store in shared preferences
                    prefEditor.putString("WEIGHT", Double.toString(weight));
                    prefEditor.commit();

                    calculateBMI();
                    BMItextView.setText(BMItoString());
                    isBMIhealthyTextView.setText(toHealthyBMIstring());
                }
                //clear BMI fields when editText is empty
                catch(IllegalArgumentException e){
                    BMItextView.setText("");
                    BMI = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
        weightEditText.addTextChangedListener(weightTextWatcher);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_userProfile) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public void setHeight(int h) {
        height = h;
    }

    public void setWeight(int w) {
        weight = w;
    }

    private void calculateBMI(){
        BMI = BMIconversionFactor*(weight/(height*height));     // 703*weight/height^2
        isHealthyBMI = false;
        if (BMI > healthyBMImin && BMI < healthyBMImax)
            isHealthyBMI = true;
    }

    private String toHealthyBMIstring(){
        if(isHealthyBMI)
            return "Your BMI is in the healthy range of " + healthyBMImin +"-" + healthyBMImax;
        else
            return "Your BMI is not in the healthy range of " + healthyBMImin +"-" + healthyBMImax;
    }

    private String BMItoString(){
        String bmiString;
        bmiString = Double.toString(BMI);
        bmiString = bmiString.substring(0,5);
        return bmiString;
    }
}
