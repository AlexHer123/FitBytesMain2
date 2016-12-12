package com.example.alex.fitbytes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

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

        Button barcodeButton = (Button) findViewById(R.id.pantryLaunchBarcode);
        barcodeButton.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void doIngredientSearch(JSONArray ingredientArray) {
        try {
            // Set when no results are found


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
        if (allIngredients.size() > 0) {
            noResults.setVisibility(View.INVISIBLE);
        } else {
            noResults.setVisibility(View.VISIBLE);
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


    String upcCode;

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                 upcCode= intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                // TODO look up ingredient by upc and add it to pantry
                new CallMashapeGetUPCAsync().execute(upcCode);


            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText( getApplicationContext(), "result canceled", Toast.LENGTH_LONG).show();
            }
        }
    }
    private String mashapeKey = "SHGsb9KyiumshnFBRwVT6uI1GXhpp1e1ymyjsn0ZMG86kcd2xg";

    protected class CallMashapeGetUPCAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        @Override
        protected void onPreExecute() {

        }

        protected HttpResponse<JsonNode> doInBackground(String... msg) {

            HttpResponse<JsonNode> request = null;
            String search = msg[0].replaceAll("\\s", "+");

            try {
                request = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/products/upc/" + upcCode)
                        .header("X-Mashape-Key", mashapeKey)
                        .header("Accept", "application/json")
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            return request;
        }

        protected void onProgressUpdate(Integer... integers) {
        }

        protected void onPostExecute(HttpResponse<JsonNode> response) {

            // Get response as object of objects
            JSONObject obj = response.getBody().getObject();
            upcIngredientInfo(obj);
        }
    }
    String ingredientName;
    @Override
    protected void upcIngredientInfo(JSONObject obj)
    {
        try {
           ingredientName = obj.getString("title");

            IngredientItem upcIngredient = new IngredientItem(ingredientName, 0, "None");
            ingredientItems = new ArrayList<>();
            ingredientItems.add(upcIngredient);
            updateIngredientList();


        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

}