package com.example.alex.fitbytes;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Alex on 12/9/2016.
 */

public abstract class PantryHandler extends MainActivity {

    private int searchAmount = 100;
    private String searchString;
    private String mashapeKey = "INSERT MASHAPE KEY HERE";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
    }

    protected class SearchIngredientCall extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait");
            dialog.show();
        }

        protected HttpResponse<JsonNode> doInBackground(String... msg) {

            HttpResponse<JsonNode> request = null;
            searchString = msg[0].replaceAll("\\s", "+");

            try {
                request = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/ingredients/autocomplete?metaInformation=false&number="+ searchAmount +"&query="+searchString)
                        .header("X-Mashape-Key", mashapeKey)
                        .header("Accept", "application/json")
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return request;
        }

        protected void onProgressUpdate(Integer...integers) {}

        protected void onPostExecute(HttpResponse<JsonNode> response) {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            // Get response as object of objects
            JSONArray obj = response.getBody().getArray();

            doIngredientSearch(obj);
        }
    }
    protected abstract void doIngredientSearch(JSONArray obj);
    protected abstract void upcIngredientInfo(JSONObject obj);
}
