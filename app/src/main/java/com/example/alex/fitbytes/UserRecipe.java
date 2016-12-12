package com.example.alex.fitbytes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserRecipe extends MainActivity {

    private DBHandler db = new DBHandler(this);

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
                EditText sodium = (EditText)findViewById(R.id.advMaxProtNum);
                EditText protein = (EditText)findViewById(R.id.editText_ur_protein);
                EditText ingredient = (EditText)findViewById(R.id.editText_ur_ingredients);
                EditText direction = (EditText)findViewById(R.id.editText_ur_directions);
                EditText aboutRecipe = (EditText)findViewById(R.id.editText_ur_recipeInfo);

                int s = Integer.parseInt(serving.getText().toString());
                int r = Integer.parseInt(readyMin.getText().toString());
                int c = Integer.parseInt(calorie.getText().toString());
                int f = Integer.parseInt(fat.getText().toString());
                int carbs = Integer.parseInt(carb.getText().toString());
                int su = Integer.parseInt(sugar.getText().toString());
                int chols = Integer.parseInt(chol.getText().toString());
                int so = Integer.parseInt(sodium.getText().toString());
                int p = Integer.parseInt(protein.getText().toString());
                String in = ingredient.getText().toString();
                String dir = direction.getText().toString();
                String about = aboutRecipe.getText().toString();

                userRecipe.setServing(s);
                userRecipe.setReadyMin(r);
                userRecipe.setCalorie(c);
                userRecipe.setFat(f);
                userRecipe.setCarbs(carbs);
                userRecipe.setSugar(su);
                userRecipe.setChol(chols);
                userRecipe.setSodium(so);
                userRecipe.setProtein(p);
                userRecipe.setIngredients(in);
                userRecipe.setDirections(dir);
                userRecipe.setAboutRecipe(about);

                db.addUserRecipe(userRecipe);

                Intent intent = new Intent(UserRecipe.this, Recipes.class);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
