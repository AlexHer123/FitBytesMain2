package com.example.alex.fitbytes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;


public class UserInfo extends MainActivity {
    private int BMI_MIN = 19;
    private int BMI_MAX = 25;
    private int BMI_CONVERSION_FACTOR = 703;
    private UserItem user;
    private String name;
    private double height, feet, inch, weight;
    private double BMI;
    private boolean isHealthyBMI;


    private TextView BMItextView;
    private TextView isBMIhealthyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        user = (UserItem) getIntent().getSerializableExtra("user");
        final EditText nameEditText = (EditText) findViewById(R.id.editText_uInfo_name);
        final EditText heightFeetEditText = (EditText) findViewById(R.id.editText_uInfo_feet);
        final EditText heightInchEditText = (EditText) findViewById(R.id.editText_uInfo_inch);
        final EditText weightEditText = (EditText) findViewById(R.id.editText_uInfo_weight);
        isBMIhealthyTextView = (TextView) findViewById(R.id.textView_uInfo_BMImessage);
        BMItextView = (TextView) findViewById(R.id.textView_uInfo_BMI);

        nameEditText.setText(user.getName());
        heightFeetEditText.setText((user.getHeight() / 12) + "");
        heightInchEditText.setText((user.getHeight() % 12)+"");
        weightEditText.setText(user.getWeight()+"");
        BMItextView.setText(Double.toString(user.getBMI()));
        feet = user.getHeight()/12;
        inch = user.getHeight()%12;
        weight = user.getWeight();
        doBMI();
        weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    weight = Double.parseDouble(weightEditText.getText().toString());
                    doBMI();
                }
                //clear BMI fields when editText is empty
                catch(IllegalArgumentException e){
                    BMItextView.setText("");
                    BMI = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        heightFeetEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    feet = Double.parseDouble(heightFeetEditText.getText().toString());
                    doBMI();
                }
                //clear BMI fields when editText is empty
                catch(IllegalArgumentException e){
                    BMItextView.setText("");
                    feet = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        heightInchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    inch = Double.parseDouble(heightInchEditText.getText().toString());
                    if (inch > 11){
                        throw new IllegalArgumentException();
                    }
                    doBMI();
                }
                //clear BMI fields when editText is empty
                catch(IllegalArgumentException e){
                    BMItextView.setText("");
                    inch = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Button doneButton = (Button) findViewById(R.id.button_uInfo_done);
        doneButton.setOnClickListener(new AdapterView.OnClickListener() {

            @Override
            public void onClick(View v) {
                name = nameEditText.getText().toString();
                feet = Double.parseDouble(heightFeetEditText.getText().toString());
                inch = Double.parseDouble(heightInchEditText.getText().toString());
                height = (feet * 12) + inch;
                weight = Double.parseDouble(weightEditText.getText().toString());
                Intent intent = new Intent(UserInfo.this, UserProfile.class);
                intent.putExtra("name", name);
                intent.putExtra("height", (int) height);
                intent.putExtra("weight", (int) weight);
                intent.putExtra("BMI", Double.parseDouble(BMItoString()));

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void doBMI() {
        calculateBMI();
        BMItextView.setText("BMI: " + BMItoString());
        isBMIhealthyTextView.setText(toHealthyBMIstring());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_userProfile) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void calculateBMI(){
        height = feet*12+inch;
        BMI = (BMI_CONVERSION_FACTOR *weight)/(height*height);     // 703*weight/height^2
        isHealthyBMI = false;
        if (BMI > BMI_MIN && BMI < BMI_MAX)
            isHealthyBMI = true;
    }

    private String toHealthyBMIstring(){
        if(isHealthyBMI)
            return "Your BMI is in the healthy range of " + BMI_MIN +"-" + BMI_MAX;
        else
            return "Your BMI is not in the healthy range of " + BMI_MIN +"-" + BMI_MAX;
    }

    private String BMItoString(){
        String bmiString;

        bmiString = Double.toString(BMI);
        int StringLength = bmiString.length();

        if(StringLength > 4)
            bmiString = bmiString.substring(0,5);
        else
            bmiString = bmiString.substring(0,StringLength);

        return bmiString;
    }
}
