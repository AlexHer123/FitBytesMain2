package com.example.alex.fitbytes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Recipes extends RecipeHandler implements SearchView.OnQueryTextListener {
    //    private String[] recipes = {"PB&J", "Ramen", "Cereal", "Grilled Cheese", "Spaghetti"};
    private List<String> recipes = new ArrayList<>();
    private DBHandler db = new DBHandler(this);
    private SearchView recipeSearchView;
    private List<RecipeItem> recipeItems = new ArrayList<>();
    private Boolean fromMP = false;
    private int selectedRecipeID;
    private int selectedRecipeCalories = 0;
    private String selectedRecipeDirections = "";
    private TextView noResults;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        noResults = (TextView) findViewById(R.id.textView_recipe_None);
        noResults.setVisibility(View.INVISIBLE);
        recipeSearchView = (SearchView) findViewById(R.id.recipeSearchView);
        fromMP = getIntent().getBooleanExtra("fromMP", false);
        setupSearchView();
        createRecipeList();

        Button cancelButton = (Button) findViewById(R.id.recPopCancel_button);
        cancelButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recipes.this, MealPlan.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
        if (!fromMP) cancelButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void doRecipeSearch(JSONObject obj) {
        try {
            // Get the objects from obj in array form
            JSONArray recipeArray = obj.getJSONArray("results");
            // Set when no results are found
            if (recipeArray.length() > 0) {
                noResults.setVisibility(View.INVISIBLE);
            } else {
                noResults.setVisibility(View.VISIBLE);
            }

            // Get the id and name of recipes
            recipeItems = new ArrayList<>();
            for (int i = 0; i < recipeArray.length(); i++) {
                JSONObject rec = recipeArray.optJSONObject(i);
                int recID = rec.getInt("id");
                String recName = rec.getString("title");
                RecipeItem tempRI = new RecipeItem(recID, recName);
                recipeItems.add(tempRI);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        createRecipeList();
    }

    @Override
    protected void fillNutrientInfo(JSONObject obj) {
        try {
            JSONArray nutrients = obj.getJSONObject("nutrition").getJSONArray("nutrients");
            JSONObject caloriesObj = nutrients.optJSONObject(0);

            selectedRecipeName = obj.getString("title");
            selectedRecipeCalories = (int) caloriesObj.getDouble("amount");
            selectedRecipeReadyTime = (int) obj.getDouble("readyInMinutes");
            selectedRecipeServings = (int) obj.getDouble("servings");

            TextView recipeName = (TextView) dialog.findViewById(R.id.mp_recipe_name);
            recipeName.setText(selectedRecipeName);

            TextView servingsText = (TextView) dialog.findViewById(R.id.recipe_servings_text);
            servingsText.setText("Servings: "+selectedRecipeServings);

            TextView readyInText = (TextView) dialog.findViewById(R.id.recipe_ready_text);
            readyInText.setText("Ready in (min): "+selectedRecipeReadyTime);

            TextView mealCalText = (TextView) dialog.findViewById(R.id.recipe_calorie_text);
            mealCalText.setText("Calories: " + selectedRecipeCalories);

            TextView directionsText = (TextView) dialog.findViewById(R.id.recipe_description_text);
            directionsText.setText("Description: TODO ");

            dialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class RecipeItem {
        private int recipeID;
        private String recipeName;

        public RecipeItem(int id, String name) {
            recipeID = id;
            recipeName = name;
        }

        public int getRecipeID() {
            return recipeID;
        }

        public String getName() {
            return recipeName;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_recipe) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //TODO should not return false
        return false;
    }

    private void createRecipeList() {
        List<String> allRecipes = new ArrayList<>();
        for (RecipeItem r : recipeItems) {
            allRecipes.add(r.getName());
        }
        ListView recipeDropdown = (ListView) findViewById(R.id.recipe_list);
        ArrayAdapter<String> recipeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allRecipes);
        recipeDropdown.setAdapter(recipeAdapter);

        // Listener for date dropdown
        recipeDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedRecipeID = recipeItems.get(position).getRecipeID();
                new CallMashapeNutrientInfoAsync().execute(selectedRecipeID + "");

                final RecipeItem recItem = recipeItems.get(position);
                dialog = new Dialog(Recipes.this);
                dialog.setTitle(parent.getItemAtPosition(position).toString());
                dialog.setContentView(R.layout.activity_recipe_popup);
                TextView mealText = (TextView) dialog.findViewById(R.id.mp_recipe_name);
                mealText.setText(recipeItems.get(position).getName());

                Button recipeSelect = (Button) dialog.findViewById(R.id.recipeSelect_button);
                recipeSelect.setOnClickListener(new AdapterView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Recipes.this, MealPlan.class);
                        intent.putExtra("recipeID", recItem.getRecipeID());
                        intent.putExtra("recipeName", recItem.getName());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
                if (!fromMP) recipeSelect.setVisibility(View.INVISIBLE);
            }


        });
    }

    private void setupSearchView() {
        recipeSearchView.setIconifiedByDefault(true);
        recipeSearchView.setSubmitButtonEnabled(true);
        recipeSearchView.setQueryHint("Search Recipes");

        recipeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // When the submit button is clicked, CallMashapeAsync is called and is passed the string we
            // are searching for
            @Override
            public boolean onQueryTextSubmit(String query) {
                new CallMashapeGetRecipeAsync().execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}