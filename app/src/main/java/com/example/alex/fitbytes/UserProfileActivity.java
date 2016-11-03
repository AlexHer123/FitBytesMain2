package com.example.alex.fitbytes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

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

        EditText heightEditText = (EditText)findViewById(R.id.userProfileUserHeight);
        EditText weightEditText = (EditText)findViewById(R.id.userProfileUserWeight);
        TextView BMItextView = (TextView)findViewById(R.id.userProfileUserBMI);
        TextView isBMIhealthyTextView = (TextView)findViewById(R.id.userProfileIsBMIhealthy);

        height = 70;
        weight = 180;
        calculateBMI();

        heightEditText.setText(""+height);
        weightEditText.setText(""+weight);
        BMItextView.setText(""+BMI);

        if(isHealthyBMI){
            isBMIhealthyTextView.setText("Your BMI is in the healthy range" + healthyBMImin +"-" + healthyBMImax);
        }
        else{
            isBMIhealthyTextView.setText("Your BMI is not in the healthy range of " + healthyBMImin +"-" + healthyBMImax);
        }

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
        if (BMI > healthyBMImin && BMI < healthyBMImax)
            isHealthyBMI = true;
    }
}
