package com.example.alex.fitbytes;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class RecipeInfo extends RecipeHandler {

    private List<ingItem> ingItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        selectedRecipeID = getIntent().getIntExtra("recipeID", 0);
        new CallMashapeNutrientInfoAsync().execute(selectedRecipeID+"");
        new CallMashapeSummaryAsync().execute(selectedRecipeID+"");
    }

    @Override
    protected void doRecipeSearch(JSONObject obj) {
        // NOT NEEDED
    }

    /*Using this to pass in the description via a different JSON obj via Summarize Recipe*/
    protected void fillRecipeInfo(JSONObject obj)
    {
        try {
            selectedRecipeDescription = stripHtmlTags(obj.getString("summary"));

            TextView recipeDescription = (TextView)findViewById(R.id.recipe_description_text);
            recipeDescription.setText(selectedRecipeDescription);

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class ingItem {
        private int ingID;
        private String ingName;

        public ingItem(int id, String name) {
            ingID = id;
            ingName = name;
        }

        public int getIngredientID() {
            return ingID;
        }

        public String getName() {
            return ingName;
        }
    }

    @Override
    protected void fillNutrientInfo(JSONObject obj) {
        try {

            JSONArray nutrients = obj.getJSONObject("nutrition").getJSONArray("nutrients");
            JSONArray ingredients = obj.getJSONArray("extendedIngredients");

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

            String imageURL = obj.getString("image");
            /*Goes through JSON array to get each ingredient's name, value, and measurement which is contained in the originalString of each ingredient JSON object*/
            for(int i=0;i<ingredients.length();i++)
            {
                String ingredientName = ingredients.getJSONObject(i).getString("originalString");
                ingItem item = new ingItem(ingredients.getJSONObject(i).getInt("id"),ingredientName);
                ingItems.add(item);
            }

            List<String> ingredientNames = new ArrayList<>();

            for(ingItem i: ingItems)
            {
                ingredientNames.add(i.getName());
            }

            ListView ingredientsList = (ListView) findViewById(R.id.ingredientList);
            List<String> test = new ArrayList<>();
            test.add(""+ingredientNames.size());
            ArrayAdapter<String> ingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientNames);
            ingredientsList.setAdapter(ingAdapter);

            /*Picasso is a library for loading images and makes it easy to load images into ImageViews*/
            ImageView recipeImage = (ImageView) findViewById(R.id.imageView);
            Picasso.with(RecipeInfo.this).load(imageURL).resize(800,800).centerInside().into(recipeImage);

            TextView recipeName = (TextView)findViewById(R.id.mp_recipe_name);
            recipeName.setText(selectedRecipeName);

            TextView servingsText = (TextView) findViewById(R.id.recipe_servings_text);
            servingsText.setText("Servings: "+selectedRecipeServings);

            TextView readyInText = (TextView) findViewById(R.id.recipe_ready_text);
            int hours = selectedRecipeReadyTime / 60;
            int minutes = selectedRecipeReadyTime % 60;
            if (hours <= 0)
            {
                readyInText.setText("Ready in: "+minutes+" min");
            }
            else
            {
                readyInText.setText("Ready in: "+hours+" hour(s) "+minutes+" min");
            }

            TextView mealCalText = (TextView) findViewById(R.id.recipe_calorie_text);
            mealCalText.setText("Calories: "+selectedRecipeCalories);

            TextView fatText = (TextView) findViewById(R.id.recipe_fat_text);
            fatText.setText("Fat: "+selectedRecipeFat+"g");

            TextView carbText = (TextView) findViewById(R.id.recipe_carb_text);
            carbText.setText("Carbs: "+selectedRecipeCarbs+"g");

            TextView sugarText = (TextView) findViewById(R.id.recipe_sugar_text);
            sugarText.setText("Sugar: "+selectedRecipeSugar+"g");

            TextView cholText = (TextView) findViewById(R.id.recipe_chol_text);
            cholText.setText("Cholesterol: "+selectedRecipeChol+"mg");

            TextView sodiumText = (TextView) findViewById(R.id.recipe_sodium_text);
            sodiumText.setText("Sodium: "+selectedRecipeSodium+"mg");

            TextView proteinText = (TextView) findViewById(R.id.recipe_protein_text);
            proteinText.setText("Protein: "+selectedRecipeProtein+"g");

            TextView mealInstructText = (TextView) findViewById(R.id.recipe_directions_text);
            selectedRecipeInstructions = selectedRecipeInstructions.replaceAll("\\s{2,}", "\n");
            mealInstructText.setText(handleStringNull(selectedRecipeInstructions));

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
