package com.example.alex.fitbytes;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class Recipes extends MainActivity implements SearchView.OnQueryTextListener
{
    private String[] recipes = {"PB&J", "Ramen", "Cereal", "Grilled Cheese", "Spaghetti"};
    private DBHandler db = new DBHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        storeAllRecipes();
        createRecipeList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_recipe)
        {
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

    private void storeAllRecipes()
    {
        for (String r: recipes)
        {
            String formatRecipe = r.toLowerCase().replaceAll("\\s", "");
            boolean added = db.addRecipe(formatRecipe, r);
        }
    }

    private void createRecipeList()
    {
        List<String> allRecipes = db.getAllRecipes();
        ListView recipeDropdown = (ListView) findViewById(R.id.recipe_list);
        ArrayAdapter<String> recipeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allRecipes);
        recipeDropdown.setAdapter(recipeAdapter);

        // Listener for date dropdown
        recipeDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("HERE: ", "DSJFLDSIJFOIDSJFOIDSJF");
                final Dialog dialog = new Dialog(Recipes.this);
                dialog.setTitle(parent.getItemAtPosition(position).toString());
                dialog.setContentView(R.layout.activity_recipe_popup);
//                TextView mealText = (TextView)dialog.findViewById(R.id.mp_recipe_name);
//                mealText.setText("ODIJFOPDSJFOIJ\nasdfhadsfklhadfkljhadsf\nasodifsfdoidjaf");
                dialog.show();
            }


        });
    }
}
