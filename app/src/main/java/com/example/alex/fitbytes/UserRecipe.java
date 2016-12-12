package com.example.alex.fitbytes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserRecipe extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recipe);

        Button doneButton = (Button)findViewById(R.id.button_ur_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText)findViewById(R.id.editText_ur_name);
                UserRecipeItem userRecipe = new UserRecipeItem(name.getText().toString());
                EditText serving = (EditText)findViewById(R.id.editText_ur_servings);
                EditText readyMin = (EditText)findViewById(R.id.editText_ur_ready);
                EditText calorie = (EditText)findViewById(R.id.editText_ur_calories);
                EditText fat = (EditText)findViewById(R.id.editText_ur_fat);
                EditText carb = (EditText)findViewById(R.id.editText_ur_carbs);
                EditText sugar = (EditText)findViewById(R.id.editText_ur_sugar);
                EditText chol = (EditText)findViewById(R.id.editText_ur_cholesterol);
                EditText sodium = (EditText)findViewById(R.id.editText_ur_sodium);
                EditText protein = (EditText)findViewById(R.id.editText_ur_protein);
                EditText ingredient = (EditText)findViewById(R.id.editText_ur_ingredients);
                EditText direction = (EditText)findViewById(R.id.editText_ur_directions);
                EditText aboutRecipe = (EditText)findViewById(R.id.editText_ur_recipeInfo);
                
            }
        });
    }
}
