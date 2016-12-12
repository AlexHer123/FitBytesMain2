package com.example.alex.fitbytes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class RecipeInfo extends RecipeHandler {

    private int USER_RECIPE_MODIFIER = 1000000;
    private List<ingItem> ingItems = new ArrayList<>();

    private DBHandler userDB = new DBHandler(this);
    private UserItem user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        selectedRecipeID = getIntent().getIntExtra("recipeID", 0);
        if (selectedRecipeID < USER_RECIPE_MODIFIER) {
            new CallMashapeSummaryAsync().execute(selectedRecipeID + "");
            new CallMashapeNutrientInfoAsync().execute(selectedRecipeID + "");

        }
        else{
            UserRecipeItem uri = userDB.getUserRecipe(selectedRecipeID-USER_RECIPE_MODIFIER);
            String[] splitIngredients = uri.getIngredients().split("\n");
            List<ingItem> ingredients = new ArrayList<>();
            for (String s : splitIngredients){
                ingredients.add(new ingItem(0, s, ""));
            }


            fillInfoHelper(uri.getName(),uri.getCalorie(),uri.getFat(),uri.getCarbs(),uri.getSugar(),uri.getChol(),uri.getSodium(),uri.getProtein(),uri.getReadyMin(),uri.getServing(),uri.getDirections(),ingredients, "https://thumbs.dreamstime.com/t/plate-question-mark-desk-red-white-fork-knife-over-57970677.jpg");
            TextView recipeDescription = (TextView)findViewById(R.id.recipe_description_text);
            recipeDescription.setText(uri.getAboutRecipe());
        }


        user = userDB.getUser();

        Button mealDoneButton = (Button)findViewById(R.id.mealIsDone);
        mealDoneButton.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(), "Meal nutrition has been added to your stats ", Toast.LENGTH_LONG).show();
                        userDB.updateUserNutrients(user.getCals()+ selectedRecipeCalories, user.getFat() + selectedRecipeFat,
                                    user.getCarbs() + selectedRecipeCarbs, user.getSugar() + selectedRecipeSugar,
                                    user.getChol() + selectedRecipeChol, user.getSodium() + selectedRecipeSodium,
                                    user.getProtein() + selectedRecipeProtein, user.getTotalMeals() + 1
                        );
            }
        });
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
        private String fullString;
        private String name;

        public ingItem(int id, String fullname, String n) {
            ingID = id;
            fullString = fullname;
            name = n;
        }

        public int getIngredientID() {
            return ingID;
        }
        public String getName() { return name; }
        public String getFullName() {
            return fullString;
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

            for(int i=0;i<ingredients.length();i++)
            {
                String ingredientFullString = ingredients.getJSONObject(i).getString("originalString");
                String ingredientName = ingredients.getJSONObject(i).getString("name");
                ingItem item = new ingItem(ingredients.getJSONObject(i).getInt("id"),ingredientFullString,ingredientName);
                ingItems.add(item);
            }

            fillInfoHelper(obj.getString("title"),(int) caloriesObj.getDouble("amount"), (int) fatObj.getDouble("amount"),(int) carbObj.getDouble("amount"),(int) sugarObj.getDouble("amount"),(int) cholObj.getDouble("amount"),(int) sodiumObj.getDouble("amount"),(int) proteinObj.getDouble("amount"),(int) obj.getDouble("readyInMinutes"),(int) obj.getDouble("servings"),obj.getString("instructions"), ingItems, obj.getString("image"));
//
//            selectedRecipeCalories = (int) caloriesObj.getDouble("amount");
//            selectedRecipeFat = (int) fatObj.getDouble("amount");
//            selectedRecipeCarbs = (int) carbObj.getDouble("amount");
//            selectedRecipeSugar = (int) sugarObj.getDouble("amount");
//            selectedRecipeChol = (int) cholObj.getDouble("amount");
//            selectedRecipeSodium = (int) sodiumObj.getDouble("amount");
//            selectedRecipeProtein = (int) proteinObj.getDouble("amount");

//            selectedRecipeReadyTime = (int) obj.getDouble("readyInMinutes");
//            selectedRecipeServings = (int) obj.getDouble("servings");
//            selectedRecipeInstructions = obj.getString("instructions");
//            selectedRecipeName = obj.getString("title");

//            String imageURL = obj.getString("image");
            /*Goes through JSON array to get each ingredient's name, value, and measurement which is contained in the originalString of each ingredient JSON object*/

//
//            List<String> ingredientNames = new ArrayList<>();
//
//            for(ingItem i: ingItems)
//            {
//                ingredientNames.add(i.getFullName());
//            }
//
//            ListView ingredientsList = (ListView) findViewById(R.id.ingredientList);
//            ArrayAdapter<String> ingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientNames)
//            {
//                @Override
//                public View getView(int position, View convertView, ViewGroup parent){
//                    // Get the current item from ListView
//                    View view = super.getView(position,convertView,parent);
//                    if(position %2 == 1)
//                    {
//                        // Set a background color for ListView regular row/item
//                        view.setBackgroundColor(Color.parseColor("#3F51B5"));
//                    }
//                    else
//                    {
//                        // Set the background color for alternate row/item
//                        view.setBackgroundColor(Color.parseColor("#303F9F"));
//                    }
//                    return view;
//                }
//            };
//            ingredientsList.setAdapter(ingAdapter);
//
//            /*Picasso is a library for loading images and makes it easy to load images into ImageViews*/
//            ImageView recipeImage = (ImageView) findViewById(R.id.imageView);
//            Picasso.with(RecipeInfo.this).load(imageURL).resize(800,800).centerInside().into(recipeImage);
//
//            TextView recipeName = (TextView)findViewById(R.id.mp_recipe_name);
//            recipeName.setText(selectedRecipeName);
//
//            TextView servingsText = (TextView) findViewById(R.id.recipe_servings_text);
//            servingsText.setText("Servings: "+selectedRecipeServings);
//
//            TextView readyInText = (TextView) findViewById(R.id.recipe_ready_text);
//            int hours = selectedRecipeReadyTime / 60;
//            int minutes = selectedRecipeReadyTime % 60;
//            if (hours <= 0)
//            {
//                readyInText.setText("Ready in: "+minutes+" min");
//            }
//            else
//            {
//                readyInText.setText("Ready in: "+hours+" hour(s) "+minutes+" min");
//            }
//
//            TextView mealCalText = (TextView) findViewById(R.id.recipe_calorie_text);
//            mealCalText.setText("Calories: "+selectedRecipeCalories);
//
//            TextView fatText = (TextView) findViewById(R.id.recipe_fat_text);
//            fatText.setText("Fat: "+selectedRecipeFat+"g");
//
//            TextView carbText = (TextView) findViewById(R.id.recipe_carb_text);
//            carbText.setText("Carbs: "+selectedRecipeCarbs+"g");
//
//            TextView sugarText = (TextView) findViewById(R.id.recipe_sugar_text);
//            sugarText.setText("Sugar: "+selectedRecipeSugar+"g");
//
//            TextView cholText = (TextView) findViewById(R.id.recipe_chol_text);
//            cholText.setText("Cholesterol: "+selectedRecipeChol+"mg");
//
//            TextView sodiumText = (TextView) findViewById(R.id.recipe_sodium_text);
//            sodiumText.setText("Sodium: "+selectedRecipeSodium+"mg");
//
//            TextView proteinText = (TextView) findViewById(R.id.recipe_protein_text);
//            proteinText.setText("Protein: "+selectedRecipeProtein+"g");
//
//            TextView mealInstructText = (TextView) findViewById(R.id.recipe_directions_text);
//            selectedRecipeInstructions = selectedRecipeInstructions.replaceAll("\\s{2,}", "\n");
//            mealInstructText.setText(handleStringNull(selectedRecipeInstructions));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillInfoHelper(String name, int cal, int fat, int carb, int sug, int chol, int sod, int pro, int read, int serv, String inst, List<ingItem> ingredients, String imageURL){
        selectedRecipeCalories = cal;
        selectedRecipeFat = fat;
        selectedRecipeCarbs = carb;
        selectedRecipeSugar = sug;
        selectedRecipeChol = chol;
        selectedRecipeSodium = sod;
        selectedRecipeProtein = pro;
        selectedRecipeReadyTime = read;
        selectedRecipeServings = serv;
        selectedRecipeInstructions = inst;
        selectedRecipeName = name;


        List<String> ingredientNames = new ArrayList<>();

        for(ingItem i: ingredients)
        {
            ingredientNames.add(i.getFullName());
        }

        ListView ingredientsList = (ListView) findViewById(R.id.ingredientList);
        ArrayAdapter<String> ingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientNames)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#3F51B5"));
                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#303F9F"));
                }
                return view;
            }
        };
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
    }

    private String handleStringNull(String value){
        if (value.equals("null") || value == null){
            return "Not provided";
        }
        return value;
    }

}
