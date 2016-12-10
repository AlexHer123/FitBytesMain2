package com.example.alex.fitbytes;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeInfo extends RecipeHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        selectedRecipeID = getIntent().getIntExtra("recipeID", 0);
        new CallMashapeNutrientInfoAsync().execute(selectedRecipeID+"");
    }

    @Override
    protected void doRecipeSearch(JSONObject obj) {
        // NOT NEEDED
    }

    @Override
    protected void fillNutrientInfo(JSONObject obj) {
        try {

            JSONArray nutrients = obj.getJSONObject("nutrition").getJSONArray("nutrients");
            JSONObject caloriesObj = nutrients.optJSONObject(0);
            JSONObject fatObj = nutrients.optJSONObject(1);
            JSONObject carbObj = nutrients.optJSONObject(3);
            JSONObject sugarObj = nutrients.optJSONObject(4);
            JSONObject cholObj = nutrients.optJSONObject(5);
            JSONObject sodiumObj = nutrients.optJSONObject(6);
            JSONObject proteinObj = nutrients.optJSONObject(7);

            selectedRecipeCalories = (int) caloriesObj.getDouble("amount");
            selectedRecipeFat = (int) fatObj.getDouble("amount");
            selectedRecipeCarbs = (int) carbObj.getDouble("amount");
            selectedRecipeSugar = (int) sugarObj.getDouble("amount");
            selectedRecipeChol = (int) cholObj.getDouble("amount");
            selectedRecipeSodium = (int) sodiumObj.getDouble("amount");
            selectedRecipeProtein = (int) proteinObj.getDouble("amount");

            selectedRecipeReadyTime = (int) obj.getDouble("readyInMinutes");
            selectedRecipeServings = (int) obj.getDouble("servings");
            selectedRecipeInstructions = obj.getString("instructions");
            selectedRecipeName = obj.getString("title");

            TextView recipeName = (TextView)findViewById(R.id.mp_recipe_name);
            recipeName.setText(selectedRecipeName);

            TextView servingsText = (TextView)findViewById(R.id.recipe_servings_text);
            servingsText.setText("Servings: "+selectedRecipeServings);

            TextView readyInText = (TextView)findViewById(R.id.recipe_ready_text);
            readyInText.setText("Ready in (min): "+selectedRecipeReadyTime);

            TextView mealCalText = (TextView)findViewById(R.id.recipe_calorie_text);
            mealCalText.setText("Calories: "+selectedRecipeCalories);

            TextView fatText = (TextView)findViewById(R.id.recipe_fat_text);
            fatText.setText("Fat: "+selectedRecipeFat);

            TextView carbText = (TextView)findViewById(R.id.recipe_carb_text);
            carbText.setText("Carbs: "+selectedRecipeCarbs);

            TextView sugarText = (TextView)findViewById(R.id.recipe_sugar_text);
            sugarText.setText("Sugar: "+selectedRecipeSugar);

            TextView cholText = (TextView)findViewById(R.id.recipe_chol_text);
            cholText.setText("Cholesterol: "+selectedRecipeChol);

            TextView sodiumText = (TextView)findViewById(R.id.recipe_sodium_text);
            sodiumText.setText("Sodium: "+selectedRecipeSodium);

            TextView proteinText = (TextView)findViewById(R.id.recipe_protein_text);
            proteinText.setText("Protein: "+selectedRecipeProtein);

            TextView mealInstructText = (TextView)findViewById(R.id.recipe_directions_text);
            selectedRecipeInstructions = selectedRecipeInstructions.replaceAll("\\s{2,}", "\n");
            mealInstructText.setText("Instructions: "+ handleStringNull(selectedRecipeInstructions));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String handleStringNull(String value){
        if (value.equals("null") || value == null){
            return "Not provided";
        }
        return value;
    }

}
