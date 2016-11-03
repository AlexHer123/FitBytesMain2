package com.example.alex.fitbytes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by shaun on 10/24/2016.
 */

public class UserProfile  extends AppCompatActivity{



    int height, weight;
    double BMI;
    boolean isHealthyBMI;

    int healthyBMImin = 19;
    int healthyBMImax = 25;
    int BMIconversionFactor = 703;

    EditText heightEditText = (EditText)findViewById(R.id.userProfileUserHeight);
    EditText weightEditText = (EditText)findViewById(R.id.userProfileUserWeight);
    TextView BMItextView = (TextView)findViewById(R.id.userProfileBMI);
    TextView isBMIhealthyTextView = (TextView)findViewById(R.id.userProfileIsBMIhealthy);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        height = 70;
        weight = 180;
        calculateBMI();

        heightEditText.setText(""+height, TextView.BufferType.EDITABLE);
        weightEditText.setText(""+weight, TextView.BufferType.EDITABLE);
        BMItextView.setText(""+BMI, TextView.BufferType.EDITABLE);

        if(isHealthyBMI){
            isBMIhealthyTextView.setText("Your BMI is in the healthy range");
        }
        else{
            isBMIhealthyTextView.setText("Your BMI is not in the healthy range");
        }

    }


    public int getHeight() {
        return height;
    }

    public int getWeight() {
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
