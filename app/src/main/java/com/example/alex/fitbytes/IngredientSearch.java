package com.example.alex.fitbytes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IngredientSearch extends PantryHandler
{
    private SearchView ingredientSearchView;
    private ListView ingredientsListView;
    private List<IngredientItem> ingredientItems = new ArrayList<>();
    private TextView noResults;
    private List<String> MEASUREMENT = Arrays.asList("None", "tsp.", "tbsp.", "cup(s)", "fl.oz.", "oz.", "pint(s)", "quart(s)", "gal.", "liter(s)", "gram(s)", "lb.", "pack(s)", "box(es)", "bag(s)");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        ingredientsListView = (ListView) findViewById(R.id.listView_ingredient_results);
        ingredientSearchView = (SearchView) findViewById(R.id.pantrySearch);
        noResults = (TextView)findViewById(R.id.textView_ingredient_None);
        setupSearchView();

        Button cancelButton = (Button)findViewById(R.id.button_ingredient_cancel);
        cancelButton.setOnClickListener(new AdapterView.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(IngredientSearch.this, Pantry.class);
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
        }
    });
    }

    @Override
    protected void doIngredientSearch(JSONArray ingredientArray) {
        try {
            // Set when no results are found
            if (ingredientArray.length() > 0) {
                noResults.setVisibility(View.INVISIBLE);
            } else {
                noResults.setVisibility(View.VISIBLE);
            }

            // Get the id and name of recipes
            ingredientItems = new ArrayList<>();
            for (int i = 0; i < ingredientArray.length(); i++) {
                JSONObject ingredientObject = ingredientArray.optJSONObject(i);
                String ingredientName = ingredientObject.getString("name");
                IngredientItem tempII = new IngredientItem(ingredientName, 0, "");
                ingredientItems.add(tempII);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        updateIngredientList();
    }

    private void updateIngredientList(){
        List<String> allIngredients = new ArrayList<>();
        for (IngredientItem i : ingredientItems) {
            allIngredients.add(i.getName());
        }
//        ListView ingredientList = (ListView) findViewById(R.id.pantryList);
        ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allIngredients);
        ingredientsListView.setAdapter(ingredientAdapter);

        ingredientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(IngredientSearch.this);
                dialog.setContentView(R.layout.activity_ingredient_popup);

                final String name = parent.getItemAtPosition(position).toString();
                TextView title = (TextView)dialog.findViewById(R.id.textView_pPop_title);
                title.setText(name);
                final EditText quantityText = (EditText)dialog.findViewById(R.id.editText_iPop_quantity);
                quantityText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
                final Spinner measurementSpinner = (Spinner)dialog.findViewById(R.id.spinner_iPop_measure);
                ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<>(IngredientSearch.this, android.R.layout.simple_list_item_1, MEASUREMENT);
                measurementSpinner.setAdapter(ingredientAdapter);

                Button addButton = (Button)dialog.findViewById(R.id.button_iPop_add);
                addButton.setOnClickListener(new AdapterView.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        int quantity = Integer.parseInt(quantityText.getText().toString());
                        String measurement = measurementSpinner.getSelectedItem().toString();
                        if (measurement.equals("None")){
                            measurement = "";
                        }
                        IngredientItem item = new IngredientItem(name, quantity, measurement);
                        dialog.dismiss();
                        Intent intent = new Intent(IngredientSearch.this, Pantry.class);
                        intent.putExtra("ingredient", item);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
                dialog.show();

            }
        });
        ingredientSearchView.clearFocus();
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

    private void setupSearchView()
    {
        ingredientSearchView.setIconifiedByDefault(false);
        ingredientSearchView.setSubmitButtonEnabled(true);
        ingredientSearchView.setQueryHint("Search Ingredients");
        ingredientSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                new SearchIngredientCall().execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    public void barcodeButtonOnClick(View view){
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
        startActivityForResult(intent, 0);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                // TODO look up ingredient by upc and add it to pantry
                Toast.makeText( getApplicationContext(), "Barcode contents = " + contents + ". Barcode type = " + format, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText( getApplicationContext(), "result canceled", Toast.LENGTH_LONG).show();
            }
        }
    }


}