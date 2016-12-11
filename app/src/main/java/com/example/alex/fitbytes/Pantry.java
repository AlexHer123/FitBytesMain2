package com.example.alex.fitbytes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Pantry extends MainActivity {

    private SearchView pantrySearchView;
    private ListView ingredientsListView;
    private List<IngredientItem> ingredientItems = new ArrayList<>();
    private DBHandler db = new DBHandler(this);
    private TextView noResults;
    private String ingredientName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        ingredientsListView = (ListView) findViewById(R.id.pantryList);
        pantrySearchView = (SearchView) findViewById(R.id.pantrySearch);
        noResults = (TextView)findViewById(R.id.textView_pantry_None);
        noResults.setVisibility(View.INVISIBLE);

        Button addButton = (Button)findViewById(R.id.button_pantry_add);
        addButton.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pantry.this, IngredientSearch.class);
                startActivityForResult(intent, 1);
            }
        });

        ingredientsListView.setTextFilterEnabled(true);
        updateIngredientList();
        setupSearchView();
    }

    private void updateIngredientList(){
        ingredientItems = db.getAllIngredients();
        List<String> allIngredients = new ArrayList<>();
        for (IngredientItem i : ingredientItems) {
            allIngredients.add(i.getName()+" : "+ i.getQuantity()+" "+i.getMeasurement());
        }
        ListView ingredientList = (ListView) findViewById(R.id.pantryList);
        ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allIngredients);
        ingredientList.setAdapter(ingredientAdapter);
        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String[] rowString = parent.getItemAtPosition(position).toString().split(" : ");
                ingredientName = rowString[0];
                final Dialog dialog = new Dialog(Pantry.this);
                dialog.setContentView(R.layout.activity_pantry_popup);
                final String[] rowItem = parent.getItemAtPosition(position).toString().split(" : ");
                final String[] rowQuantity = rowItem[1].split(" ");
                final String name = rowItem[0];
                final String quantity = rowQuantity[0];
                final String measurement;
                if(rowQuantity.length==1)
                {
                    measurement = "";
                }
                else
                {
                    measurement = rowQuantity[1];
                }
                TextView title = (TextView)dialog.findViewById(R.id.textView_pPop_title);
                title.setText(name);
                final EditText quantityText = (EditText)dialog.findViewById(R.id.editText_pPop_quantity);
                quantityText.setText(quantity);
                quantityText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
                final TextView measureText = (TextView)dialog.findViewById(R.id.textView_pPop_measure);
                measureText.setText(measurement);

                Button deleteButton = (Button) dialog.findViewById(R.id.button_pPop_delete);
                deleteButton.setOnClickListener(new AdapterView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        db.removeIngredient(ingredientName);
                        updateIngredientList();
                        dialog.dismiss();
                    }
                });

                Button doneButton = (Button) dialog.findViewById(R.id.button_pPop_add);
                doneButton.setText("Done");
                doneButton.setOnClickListener(new AdapterView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int tempQuantity = Integer.parseInt(quantityText.getText().toString());
                        IngredientItem item = new IngredientItem(name, tempQuantity, measurement);
                        db.updatePantryIngredient(item);
                        dialog.dismiss();
                        updateIngredientList();
                    }
                });
                dialog.show();
            }
        });
        pantrySearchView.clearFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_pantry)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Handles return from IngredientSearch
        if (requestCode == 1) {
            // Set the selected date values
            if (resultCode == Activity.RESULT_OK) {
                IngredientItem item = (IngredientItem)intent.getSerializableExtra("ingredient");
                db.addIngredient(item);
                updateIngredientList();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void setupSearchView()
    {
        pantrySearchView.setIconifiedByDefault(false);
        pantrySearchView.setSubmitButtonEnabled(true);
        pantrySearchView.setQueryHint("Search Pantry");
        pantrySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            ArrayAdapter<String> ingredientAdapter = (ArrayAdapter<String>)ingredientsListView.getAdapter();

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)) {
                    ingredientAdapter.getFilter().filter(null);
                }
                else {
                    ingredientAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }
}