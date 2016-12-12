package com.example.alex.fitbytes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

public class Recipes extends RecipeHandler implements SearchView.OnQueryTextListener {
    //    private String[] recipes = {"PB&J", "Ramen", "Cereal", "Grilled Cheese", "Spaghetti"};
    //private List<String> recipes = new ArrayList<>();
    private int USER_RECIPE_MODIFIER = 1000000;

    private DBHandler db = new DBHandler(this);
    private SearchView recipeSearchView;
    private List<RecipeItem> recipeItems = new ArrayList<>();
    private List<String> recipeList = new ArrayList<>();
//    private List<RecipeItem> uri = new ArrayList<>();
    private Boolean fromMP = false;
    private int selectedRecipeID;
    private int selectedRecipeCalories = 0;
    private TextView noResults;
    private Dialog dialog;
    private String queryString;

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

        Button createButton = (Button) findViewById(R.id.create_button);
        createButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recipes.this, UserRecipe.class);
                startActivityForResult(intent, 1);
            }
        });

        if (!fromMP) cancelButton.setVisibility(View.INVISIBLE);
        else createButton.setVisibility(View.INVISIBLE);
    }

    /*Using this to pass in the description via a different JSON obj via Summarize Recipe*/
    @Override
    protected void fillRecipeInfo(JSONObject obj)
    {
        try {
            selectedRecipeDescription = stripHtmlTags(obj.getString("summary"));

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRecipeSearch(JSONObject obj) {
        try {

            List<UserRecipeItem> uTemp = db.getAllUserRecipe(queryString);
            recipeItems = new ArrayList<>();
            for (UserRecipeItem u : uTemp){
                recipeItems.add(new RecipeItem(u.getID()+USER_RECIPE_MODIFIER, u.getName(), 1));
            }

            List<RecipeItem> dTemp = db.getSelectedDefaultRecipes(queryString);
            for (RecipeItem r: dTemp){
                recipeItems.add(r);
            }

            // Get the objects from obj in array form
            JSONArray recipeArray = obj.getJSONArray("results");

            // Get the id and name of recipes

            for (int i = 0; i < recipeArray.length(); i++) {
                JSONObject rec = recipeArray.optJSONObject(i);
                int recID = rec.getInt("id");
                String recName = rec.getString("title");
                RecipeItem tempRI = new RecipeItem(recID, recName, 2);
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

            String title = obj.getString("title");
            int amount = (int) caloriesObj.getDouble("amount");
            int readyInMinutes = (int) obj.getDouble("readyInMinutes");
            int servings = (int) obj.getDouble("servings");
            String image = obj.getString("image");
            fillInfoHelper(title, amount, readyInMinutes, servings, image);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillInfoHelper(String title, int cal, int ready, int serve, String imageURL){
        selectedRecipeName = title;
        selectedRecipeCalories = cal;
        selectedRecipeReadyTime = ready;
        selectedRecipeServings = serve;

        /*Picasso is a library for loading images and makes it easy to load images into ImageViews*/
        ImageView recipeImage = (ImageView) dialog.findViewById(R.id.imageView);
        if (!imageURL.equals("None")) {
            Picasso.with(Recipes.this).load(imageURL).resize(800, 800).centerInside().into(recipeImage);
        }
        else{

        }
        TextView servingsText = (TextView) dialog.findViewById(R.id.recipe_servings_text);
        servingsText.setText("Servings: " + selectedRecipeServings);

        TextView readyInText = (TextView) dialog.findViewById(R.id.recipe_ready_text);
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

        TextView mealCalText = (TextView) dialog.findViewById(R.id.recipe_calorie_text);
        mealCalText.setText("Calories: " + selectedRecipeCalories);

        TextView descriptionText = (TextView) dialog.findViewById(R.id.recipe_description_text);
        if(selectedRecipeDescription.isEmpty())
        {
            descriptionText.setText("No Description Found");
        }
        else {
            descriptionText.setText(selectedRecipeDescription);
        }

        dialog.show();
    }
//    private class RecipeItem {
//        private int recipeID;
//        private String recipeName;
//        private int type; // 0 = Default, 1 = user, 2 = API
//
//        public int getType() {
//            return type;
//        }
//
//        public void setType(int type) {
//            this.type = type;
//        }
//
//        public RecipeItem(int id, String name, int t) {
//            recipeID = id;
//            recipeName = name;
//            type = t;
//        }
//
//        public int getRecipeID() {
//            return recipeID;
//        }
//
//        public String getName() {
//            return recipeName;
//        }
//    }

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
        /*For API recipe items*/
        final List<String> allRecipes = new ArrayList<>();
//        for (RecipeItem u : uri){
//            allRecipes.add(u.getName());
//        }
        for (RecipeItem r : recipeItems) {
            allRecipes.add(r.getName());
        }

        if (allRecipes.size() > 0) {
            noResults.setVisibility(View.INVISIBLE);
        } else {
            noResults.setVisibility(View.VISIBLE);
        }
//        final List<String> userRecipes = new ArrayList<>();
        ListView recipeDropdown = (ListView) findViewById(R.id.recipe_list);

        ArrayAdapter<String> recipeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allRecipes);
        recipeDropdown.setAdapter(recipeAdapter);
        recipeSearchView.clearFocus();

        // Listener for date dropdown
        recipeDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RecipeItem recItem = recipeItems.get(position);
                selectedRecipeID = recItem.getRecipeID();

                dialog = new Dialog(Recipes.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_recipe_popup);
                if (recItem.getType() == 1){
                    UserRecipeItem uri = db.getUserRecipe(recItem.getRecipeID()-USER_RECIPE_MODIFIER);
                    selectedRecipeDescription = uri.getAboutRecipe();
                    fillInfoHelper(uri.getName(), uri.getCalorie(), uri.getReadyMin(), uri.getServing(), "https://thumbs.dreamstime.com/t/plate-question-mark-desk-red-white-fork-knife-over-57970677.jpg");
                }
                else {
                    new CallMashapeSummaryAsync().execute(selectedRecipeID + "");
                    new CallMashapeNutrientInfoAsync().execute(selectedRecipeID + "");
                }

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
        recipeSearchView.setIconifiedByDefault(false);
        recipeSearchView.setSubmitButtonEnabled(false);
        recipeSearchView.setQueryHint("Search Recipes");

        recipeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // When the submit button is clicked, CallMashapeAsync is called and is passed the string we
            // are searching for
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryString = query;
                new CallMashapeGetRecipeAsync().execute(queryString);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Handles return from User Recipe
        if (requestCode == 1) {
            // Set the selected date values
            if (resultCode == Activity.RESULT_OK) {
                // Nothing
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}