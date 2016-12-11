package com.example.alex.fitbytes;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public abstract class RecipeHandler extends MainActivity {

    private String mashapeKey = "SHGsb9KyiumshnFBRwVT6uI1GXhpp1e1ymyjsn0ZMG86kcd2xg";
    protected int selectedRecipeID;
    protected String selectedRecipeName;
    protected int selectedRecipeCalories = 0;
    protected int selectedRecipeFat = 0;
    protected int selectedRecipeCarbs = 0;
    protected int selectedRecipeSugar = 0;
    protected int selectedRecipeChol = 0;
    protected int selectedRecipeSodium = 0;
    protected int selectedRecipeProtein = 0;
    protected int selectedRecipeReadyTime = 0;
    protected int selectedRecipeServings = 0;
    protected String selectedRecipeInstructions = "";
    protected String selectedRecipeDescription = "";
    private ProgressDialog dialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("RecipeHandler Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    protected class CallMashapeGetRecipeAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait");
            dialog.show();
        }

        protected HttpResponse<JsonNode> doInBackground(String... msg) {

            HttpResponse<JsonNode> request = null;
            String search = msg[0].replaceAll("\\s", "+");

            try {
                request = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?number=10&offset=0&query=" + search)
                        .header("X-Mashape-Key", mashapeKey)
                        .header("accept", "application/json")
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            return request;
        }

        protected void onProgressUpdate(Integer... integers) {
        }

        protected void onPostExecute(HttpResponse<JsonNode> response) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            // Get response as object of objects
            JSONObject obj = response.getBody().getObject();
            doRecipeSearch(obj);
        }
    }

    protected class CallMashapeNutrientInfoAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        // Show loading dialog
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait");
            dialog.show();
        }

        protected HttpResponse<JsonNode> doInBackground(String... msg) {
            HttpResponse<JsonNode> request = null;
            //ID of recipe that was clicked on in Recipes results list
            selectedRecipeID = Integer.parseInt(msg[0]);

            // Executes the search call
            try {
                //Search request for 'Get Recipe Information' method of api
                request = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + selectedRecipeID + "/information?includeNutrition=true")
                        .header("X-Mashape-Key", "SHGsb9KyiumshnFBRwVT6uI1GXhpp1e1ymyjsn0ZMG86kcd2xg")
                        .header("Accept", "application/json")
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return request;
        }

        protected void onProgressUpdate(Integer... integers) {
        }

        // Prints the results of the search
        protected void onPostExecute(HttpResponse<JsonNode> response) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            JSONObject obj = response.getBody().getObject();
            fillNutrientInfo(obj);
        }
    }

    protected class CallMashapeSummaryAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

        // Show loading dialog
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait");
            dialog.show();
        }

        protected HttpResponse<JsonNode> doInBackground(String... msg) {
            HttpResponse<JsonNode> request = null;
            //ID of recipe that was clicked on in Recipes results list
            selectedRecipeID = Integer.parseInt(msg[0]);

            // Executes the search call
            try {
                //Search request for 'Summarize Recipe' method of api
                request = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/45662/summary")
                        .header("X-Mashape-Key", "SHGsb9KyiumshnFBRwVT6uI1GXhpp1e1ymyjsn0ZMG86kcd2xg")
                        .header("Accept", "application/json")
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return request;
        }

        protected void onProgressUpdate(Integer... integers) {
        }

        // Prints the results of the search
        protected void onPostExecute(HttpResponse<JsonNode> response) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            JSONObject obj = response.getBody().getObject();
            fillRecipeInfo(obj);
        }
    }

    // Remove HTML tags from a string of text
    public String stripHtmlTags(String html) {
        return Html.fromHtml(html).toString();
    }

    protected abstract void doRecipeSearch(JSONObject obj);

    protected abstract void fillNutrientInfo(JSONObject obj);

    protected abstract void fillRecipeInfo(JSONObject obj);

}
