package com.example.alex.fitbytes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipes extends RecipeHandler implements SearchView.OnQueryTextListener
{
    //    private String[] recipes = {"PB&J", "Ramen", "Cereal", "Grilled Cheese", "Spaghetti"};
    private List<String> recipes = new ArrayList<>();
    private DBHandler db = new DBHandler(this);
    private SearchView recipeSearchView;
    private List<RecipeItem> recipeItems = new ArrayList<>();
    private Boolean fromMP = false;
    private int selectedRecipeID;
    private int selectedRecipeCalories = 0;
    private String selectedRecipeInstructions = "";
    private TextView noResults;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        noResults = (TextView)findViewById(R.id.textView_recipe_None);
        noResults.setVisibility(View.INVISIBLE);
        recipeSearchView = (SearchView)findViewById(R.id.recipeSearchView);
        fromMP = getIntent().getBooleanExtra("fromMP", false);
        setupSearchView();
        createRecipeList();

        Button cancelButton = (Button)findViewById(R.id.recPopCancel_button);
        cancelButton.setOnClickListener(new AdapterView.OnClickListener(){
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
            if (recipeArray.length() > 0){
                noResults.setVisibility(View.INVISIBLE);
            }
            else{
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
        // DO LATER
    }

    private class RecipeItem {
        private int recipeID;
        private String recipeName;

        public RecipeItem(int id, String name){
            recipeID = id;
            recipeName = name;
        }
        public void setID(int id){
            recipeID = id;
        }

        public void setRecipeName(String name){
            recipeName = name;
        }

        public int getRecipeID(){
            return recipeID;
        }
        public String getName(){
            return recipeName;
        }

        public void printRecipe() {
            Log.d("Here is Recipe: ", recipeID + " " +recipeName);
        }

    }
    // Not 100% sure how this class works. I just copied it from http://blog.mashape.com/using-unirest-java-for-your-android-projects/
//    private class CallMashapeAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {
//
//        // Happens when you use CallMashape().execute(string)  --> call happens in button listener below
//        protected HttpResponse<JsonNode> doInBackground(String... msg) {
//
//            HttpResponse<JsonNode> request = null;
//            String search = msg[0].replaceAll("\\s", "+");
//
//            // Executes the search call
//            try {
//                request = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?number=10&offset=0&query=" + search)
//                        .header("X-Mashape-Key", "SHGsb9KyiumshnFBRwVT6uI1GXhpp1e1ymyjsn0ZMG86kcd2xg")
//                        .header("accept", "application/json")
//                        .asJson();
//            } catch (UnirestException e) {
//                e.printStackTrace();
//            }
//
//            return request;
//        }
//
//        protected void onProgressUpdate(Integer...integers) {
//        }
//
//        // Not sure when this happens.
//        // Prints the results of the search
//        protected void onPostExecute(HttpResponse<JsonNode> response) {
//            // Get response as object of objects
//            JSONObject obj = response.getBody().getObject();
//            try {
//                // Get the objects from obj in array form
//                JSONArray recipeArray = obj.getJSONArray("results");
//                // Set when no results are found
//                if (recipeArray.length() > 0){
//                    noResults.setVisibility(View.INVISIBLE);
//                }
//                else{
//                    noResults.setVisibility(View.VISIBLE);
//                }
//
//                // Get the id and name of recipes
//                recipeItems = new ArrayList<>();
//                for (int i = 0; i < recipeArray.length(); i++) {
//                    JSONObject rec = recipeArray.optJSONObject(i);
//                    int recID = rec.getInt("id");
//                    String recName = rec.getString("title");
//                    RecipeItem tempRI = new RecipeItem(recID, recName);
//                    recipeItems.add(tempRI);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            createRecipeList();
//        }
//    }

    // Not 100% sure how this class works. I just copied it from http://blog.mashape.com/using-unirest-java-for-your-android-projects/
    private class CallMashapeNutrientInfoAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        // Happens when you use CallMashape().execute(string)  --> call happens in button listener below
        protected HttpResponse<JsonNode> doInBackground(String... msg) {

            String search = msg[0].replaceAll("\\s", "+");
            HttpResponse<JsonNode> request = null;
            //ID of recipe that was clicked on in Recipes results list
            int id = selectedRecipeID;

            // Executes the search call
            try {
                //Search request for 'Get Recipe Information' method of api
                request = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"+id+"/information?includeNutrition=true")
                        .header("X-Mashape-Key", "SHGsb9KyiumshnFBRwVT6uI1GXhpp1e1ymyjsn0ZMG86kcd2xg")
                        .header("Accept", "application/json")
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return request;
        }

        protected void onProgressUpdate(Integer...integers) {
        }

        // Prints the results of the search
        protected void onPostExecute(HttpResponse<JsonNode> response) {
            JSONObject obj = response.getBody().getObject();
            try {
                JSONArray nutrients = obj.getJSONObject("nutrition").getJSONArray("nutrients");
                JSONObject caloriesObj = nutrients.optJSONObject(0);
                selectedRecipeCalories = (int) caloriesObj.getDouble("amount");
                selectedRecipeInstructions = obj.getString("instructions");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //selectedRecipeInstructions = selectedRecipeID+"";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_recipe) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        //TODO should not return false
        return false;
    }

    private void createRecipeList()
    {
        List<String> allRecipes = new ArrayList<>();
        for (RecipeItem r : recipeItems) {
            allRecipes.add(r.getName());
        }
        ListView recipeDropdown = (ListView) findViewById(R.id.recipe_list);
        ArrayAdapter<String> recipeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allRecipes);
        recipeDropdown.setAdapter(recipeAdapter);

        // Listener for date dropdown
        recipeDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RecipeItem recItem = recipeItems.get(position);
                final Dialog dialog = new Dialog(Recipes.this);
                dialog.setTitle(parent.getItemAtPosition(position).toString());
                dialog.setContentView(R.layout.activity_recipe_popup);
                TextView mealText = (TextView)dialog.findViewById(R.id.mp_recipe_name);
                mealText.setText(recipeItems.get(position).getName());

                selectedRecipeID = recipeItems.get(position).getRecipeID();

                new CallMashapeNutrientInfoAsync().execute(selectedRecipeID+"");

                TextView mealCalText = (TextView)dialog.findViewById(R.id.recipe_calorie_text);
                mealCalText.setText("Calories: "+selectedRecipeCalories);
                TextView mealInstructText = (TextView)dialog.findViewById(R.id.recipe_ingredient_text);
                selectedRecipeInstructions = selectedRecipeInstructions.replaceAll("\\s{2,}", "\n");
                mealInstructText.setText("Instructions: "+selectedRecipeInstructions);

                dialog.show();

                Button recipeSelect = (Button)dialog.findViewById(R.id.recipeSelect_button);
                recipeSelect.setOnClickListener(new AdapterView.OnClickListener(){
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

    private void setupSearchView()
    {
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