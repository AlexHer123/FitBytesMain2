package com.example.alex.fitbytes;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.text.TextUtils;
import android.widget.ListView;
import java.util.ArrayList;
import static com.example.alex.fitbytes.IngredientItem.Measurement.*;

public class Pantry extends MainActivity implements SearchView.OnQueryTextListener
{
    private SearchView pantrySearchView;
    private ListView ingredientsListView;
    private ArrayList<IngredientItem> ingredients;
    private IngredientAdapter ingredientAdapter;
    private DBHandler db = new DBHandler(this);
    private boolean editIngredient = false;
    private int editVisible = View.INVISIBLE;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        ingredientsListView = (ListView) findViewById(R.id.pantryList);
        pantrySearchView = (SearchView) findViewById(R.id.pantrySearch);

        ingredients = new ArrayList<>();
        ingredients.add(new IngredientItem("Peanut Butter",8, OZ));
        ingredients.add(new IngredientItem("Jelly",8, OZ));
        ingredients.add(new IngredientItem("Bread",8, NONE));
        ingredients.add(new IngredientItem("Milk",128, OZ));
        ingredients.add(new IngredientItem("Cheerios",2, CUP));
        ingredients.add(new IngredientItem("Water",10,CUP));
        ingredients.add(new IngredientItem("Ramen pack",1,NONE));
        ingredients.add(new IngredientItem("Ramen seasoning",1,NONE));
        ingredients.add(new IngredientItem("Cheese(slice)",2,NONE));
        ingredients.add(new IngredientItem("Butter",1, TBSP));
        ingredients.add(new IngredientItem("Salt",1,TBSP));
        ingredients.add(new IngredientItem("Tomato Sauce",15,OZ));
        ingredients.add(new IngredientItem("Spaghetti noodles",16,OZ));

        ingredientAdapter = new IngredientAdapter(Pantry.this,ingredients);
        ingredientsListView.setAdapter(ingredientAdapter);

        ingredientsListView.setTextFilterEnabled(true);
        setupSearchView();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_pantry) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSearchView()
    {
        pantrySearchView.setIconifiedByDefault(false);
        pantrySearchView.setOnQueryTextListener(this);
        pantrySearchView.setSubmitButtonEnabled(true);
        pantrySearchView.setQueryHint("Search Ingredients");
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        if(TextUtils.isEmpty(newText))
        {
            ingredientsListView.clearTextFilter();
        }
        else
        {
            ingredientsListView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    };
}