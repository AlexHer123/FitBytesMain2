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
            selectedRecipeCalories = (int) caloriesObj.getDouble("amount");
            selectedRecipeInstructions = obj.getString("instructions");
            selectedRecipeName = obj.getString("title");
            TextView recipeName = (TextView)findViewById(R.id.mp_recipe_name);
            recipeName.setText(selectedRecipeName);
            TextView mealCalText = (TextView)findViewById(R.id.recipe_calorie_text);
            mealCalText.setText("Calories: "+selectedRecipeCalories);
            TextView mealInstructText = (TextView)findViewById(R.id.recipe_ingredient_text);
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
