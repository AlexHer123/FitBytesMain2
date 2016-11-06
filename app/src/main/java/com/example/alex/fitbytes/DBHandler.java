package com.example.alex.fitbytes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alex on 10/31/2016.
 */

public class DBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fitbytes.db";
    private static final String TABLE_MEALPLAN = "mealPlan";
    private static final String col_1_ID = "ID";
    private static final String col_2_DATE = "Date";
    private static final String col_3_RECIPE = "Recipe";


    private static final String GOAL_ID = "goal_id";
    private static final String GOAL_DESCRIPTION = "goal_description";
    private static final String GOAL_DATE = "goal_date";
    private static final String GOAL_DURATION = "goal_duration";

    private static final String TABLE_DATE = "currentDate";
    private static final String TD_col_1_DATE = "Date";

    private static final String TABLE_FITNESS_TRACKER = "fitnessTracker";

    //private static final String TABLE_INGREDIENTS = "ingredient";
    //private static final String TI_1_NAME = "Name";
    //private static final String TI_2_DATE = "Date";
    //private static final String TI_3_RECIPE = "Recipe";

    private static final String TABLE_RECIPES = "recipe";
    private static final String TR_col_1_ID = "ID";
    private static final String TR_col_2_NAME = "newName";
    private static final String TR_col_3_ORIGINALNAME = "originalName";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_MEALPLAN_TABLE = "CREATE TABLE " + TABLE_MEALPLAN + "("
                + col_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_2_DATE + " INTEGER, " + col_3_RECIPE + " INTEGER" + ")";
        db.execSQL(CREATE_MEALPLAN_TABLE);

        String CREATE_CURRENT_DATE_TABLE = "CREATE TABLE " + TABLE_DATE + " (" + TD_col_1_DATE + " INTEGER PRIMARY KEY)";
        db.execSQL(CREATE_CURRENT_DATE_TABLE);

        String CREATE_FITNESS_TRACKER_TABLE =
                String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER)",
                        TABLE_FITNESS_TRACKER, GOAL_ID, GOAL_DESCRIPTION, GOAL_DATE, GOAL_DURATION);
        db.execSQL(CREATE_FITNESS_TRACKER_TABLE);

        String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + TR_col_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TR_col_2_NAME + " TEXT, " + TR_col_3_ORIGINALNAME + " TEXT" + ")";
        db.execSQL(CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALPLAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FITNESS_TRACKER);
        onCreate(db);
    }

    public boolean addGoal(String description, int date, int duration){
        /*String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = %s",
                TABLE_FITNESS_TRACKER,
                GOAL_DATE,
                date
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(!cursor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(GOAL_DATE, date);
            values.put(GOAL_DESCRIPTION, description);
            values.put(GOAL_DURATION, duration);
            db.insert(TABLE_FITNESS_TRACKER, null, values);
            return true;
        }
        return false;*/
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GOAL_DESCRIPTION, description);
        values.put(GOAL_DATE, date);
        values.put(GOAL_DURATION, duration);
        db.insert(TABLE_FITNESS_TRACKER, null, values);
        return true;
    }

    public List<Goal> getAllGoals(){
        List<Goal> list = new ArrayList<>();
        String selectQuery = String.format(
                "SELECT * FROM %s ORDER BY %s ASC",
                TABLE_FITNESS_TRACKER,
                GOAL_DATE
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                String description = cursor.getString(1);
                int date = cursor.getInt(2);
                int duration = cursor.getInt(3);
                Goal g = new Goal(description, date, duration);
                list.add(g);
            } while(cursor.moveToNext());
        }
        //cursor.close();
        return list;
    }

    public void removeGoal(int date){
        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = %s",
                TABLE_FITNESS_TRACKER,
                GOAL_DATE,
                date
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int goalID = cursor.getInt(0);
            db.delete(TABLE_FITNESS_TRACKER, GOAL_ID + " = ?", new String[]{String.valueOf(goalID)});
        }
        cursor.close();
    }

    // Adding new meal
    public boolean addPlan(int date, int recipe)
    {
        // Search for duplicate date
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + col_2_DATE + " = " + date + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Only add if date does not exist
        if (!cursor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(col_2_DATE, date);
            values.put(col_3_RECIPE, recipe);
            // Inserting Row
            db.insert(TABLE_MEALPLAN, null, values);
            return true;
        }
        return false;
    }

    // Get the recipe for a meal plan
    public int getMealRecipe(int date)
    {
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + col_2_DATE + " = " + date + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
            return cursor.getInt(2);
        return -1;
    }



    // Getting All Meal Plans
    public List<String> getAllPlans()
    {
        List<String> planList = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " ORDER BY " + col_2_DATE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                planList.add(cursor.getInt(1) + " " + cursor.getInt(2));
            } while (cursor.moveToNext());
        }

        return planList;
    }

    public void removePlan(int date)
    {
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + col_2_DATE + " = " + date + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int planID = cursor.getInt(0);
            db.delete(TABLE_MEALPLAN, col_1_ID + " = ?", new String[]{String.valueOf(planID)});
        }
    }

    public void addCurrentDate(int date)
    {
        // Search for duplicate date
        String selectQuery = "SELECT * FROM " + TABLE_DATE;// + " WHERE " + col_2_DATE + " = '" + date + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Remove old date
        if (cursor.moveToFirst()) {
            int planID = cursor.getInt(0);
            db.delete(TABLE_DATE, TD_col_1_DATE + " = ?", new String[]{String.valueOf(planID)});
        }
        // Add new current date
        ContentValues values = new ContentValues();
        values.put(TD_col_1_DATE, date);
        // Inserting Row
        db.insert(TABLE_DATE, null, values);
    }

    public int getCurrentDate()
    {
        int date = -1;

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_DATE;// + " ORDER BY " + TD_col_1_DATE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
                date = cursor.getInt(0);
        }
        return date;
    }

    public boolean addRecipe(String formatName, String oldName){
        // Search for duplicate recipe
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES + " WHERE " + TR_col_2_NAME + " = '" + formatName + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Only add if recipe does not exist
        if (!cursor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(TR_col_2_NAME, formatName);
            values.put(TR_col_3_ORIGINALNAME, oldName);
            // Inserting Row
            db.insert(TABLE_RECIPES, null, values);

            return true;
        }
        return false;
    }

    public String getRecipe(String name){
        // Search for recipe
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES + " WHERE " + TR_col_2_NAME + " = '" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            return cursor.getString(1);
        }
        return "NONE";
    }

    public List<String> getAllRecipes(){
        List<String> recipeList = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                recipeList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        return recipeList;

    }

    public void addIngredient()
    {

    }

    public void removeIngredient()
    {

    }

    public List<IngredientItem> getAllIngredients()
    {
        List<IngredientItem> ingredientList = new ArrayList<IngredientItem>();

        return ingredientList;
    }

}