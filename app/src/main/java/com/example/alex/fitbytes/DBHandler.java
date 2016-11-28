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
    private static final String MP_col_1_ID = "ID";
    private static final String MP_col_2_DATE = "Date";
    private static final String MP_col_3_RECIPEID = "RecipeID";
    private static final String MP_col_4_RECIPENAME = "RecipeName";


    private static final String GOAL_ID = "goal_id";
    private static final String GOAL_DESCRIPTION = "goal_description";
    private static final String GOAL_DATE = "goal_date";
    private static final String GOAL_DURATION = "goal_duration";
    private static final String GOAL_COMPLETED = "goal_completed";
    private static final String GOAL_TYPE = "goal_type";

    private static final String TABLE_DATE = "currentDate";
    private static final String TD_col_1_DATE = "Date";

    private static final String TABLE_FITNESS_TRACKER = "fitnessTracker";

    private static final String TABLE_INGREDIENTS = "ingredient";
    private static final String TI_col_1_ID = "ID";
    private static final String TI_col_2_NAME = "Name";
    private static final String TI_col_3_AMOUNT = "Amount";
    private static final String TI_col_4_MEASUREMENT = "Measurement";

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
            + MP_col_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + MP_col_2_DATE + " INTEGER, " + MP_col_3_RECIPEID + " INTEGER, " + MP_col_4_RECIPENAME + " TEXT)";
        db.execSQL(CREATE_MEALPLAN_TABLE);

        String CREATE_CURRENT_DATE_TABLE = "CREATE TABLE " + TABLE_DATE + " (" + TD_col_1_DATE + " INTEGER PRIMARY KEY)";
        db.execSQL(CREATE_CURRENT_DATE_TABLE);

        String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + TR_col_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TR_col_2_NAME + " TEXT, " + TR_col_3_ORIGINALNAME + " TEXT" + ")";
        db.execSQL(CREATE_RECIPE_TABLE);

        String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + "("
                + TI_col_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TI_col_2_NAME + " TEXT, " + TI_col_3_AMOUNT + " REAL, " + TI_col_4_MEASUREMENT + " TEXT" + ")";
        db.execSQL(CREATE_INGREDIENTS_TABLE);


        String CREATE_FITNESS_TRACKER_TABLE =
                String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER, %s BOOLEAN, %s TEXT)",
                        TABLE_FITNESS_TRACKER, GOAL_ID, GOAL_DESCRIPTION, GOAL_DATE, GOAL_DURATION, GOAL_COMPLETED, GOAL_TYPE);
        db.execSQL(CREATE_FITNESS_TRACKER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALPLAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FITNESS_TRACKER);
        onCreate(db);
    }

    public boolean addGoal(String description, int date, int duration, boolean completed, Goal.Type type){
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
        values.put(GOAL_COMPLETED, completed);
        values.put(GOAL_TYPE, type.toString());
        db.insert(TABLE_FITNESS_TRACKER, null, values);
        return true;
    }
    public Goal getGoal(String name){
        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = '%s'", TABLE_FITNESS_TRACKER, GOAL_DESCRIPTION, name
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String description = cursor.getString(1);
        int date = cursor.getInt(2);
        int duration = cursor.getInt(3);

        Goal g = null;
        switch(cursor.getString(5)) {
            case "DAILY":
                g = new DailyGoal(description, date, duration);
                break;
            case "WEEKLY":
                g = new WeeklyGoal(description, date, duration);
                break;
            case "USER":
                g = new UserGoal(description, date, duration);
        }
        g.setCompleted(cursor.getInt(4) > 0);
        return g;
    }

    public void setGoalCompleted(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s SET %s = %s WHERE %s = '%s'",
                TABLE_FITNESS_TRACKER,
                GOAL_COMPLETED,
                1,
                GOAL_DESCRIPTION,
                name));
    }
    public void setGoalIncompleted(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s SET %s = %s WHERE %s = '%s'",
                TABLE_FITNESS_TRACKER,
                GOAL_COMPLETED,
                0,
                GOAL_DESCRIPTION,
                name));
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
                boolean completed = cursor.getInt(4) > 0;
                Goal g = null;
                Log.d("error: ", cursor.getString(5));
                switch(cursor.getString(5)) {
                    case "DAILY":
                        g = new DailyGoal(description, date, duration);
                        break;
                    case "WEEKLY":
                        g = new WeeklyGoal(description, date, duration);
                        break;
                    case "USER":
                        g = new UserGoal(description, date, duration);
                }
                g.setCompleted(completed);
                list.add(g);
            } while(cursor.moveToNext());
        }
        return list;
    }
    public List<Goal> getAllGoals(int dur){
        List<Goal> list = new ArrayList<>();
        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = %s ORDER BY %s ASC",
                TABLE_FITNESS_TRACKER,
                GOAL_DURATION,
                dur,
                GOAL_DATE
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                String description = cursor.getString(1);
                int date = cursor.getInt(2);
                int duration = cursor.getInt(3);
                boolean completed = cursor.getInt(4) > 0;
                Goal g = null;
                switch(cursor.getString(5)) {
                    case "DAILY":
                        g = new DailyGoal(description, date, duration);
                        break;
                    case "WEEKLY":
                        g = new WeeklyGoal(description, date, duration);
                        break;
                    case "USER":
                        g = new UserGoal(description, date, duration);
                }
                g.setCompleted(completed);
                list.add(g);
            } while(cursor.moveToNext());
        }
        return list;
    }

//    public void removeGoal(String description){
//        String selectQuery = String.format(
//                "SELECT * FROM %s WHERE %s = %s",
//                TABLE_FITNESS_TRACKER,
//                GOAL_DESCRIPTION,
//                description
//        );
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            int goalID = cursor.getInt(0);
//            db.delete(TABLE_FITNESS_TRACKER, GOAL_ID + " = ?", new String[]{String.valueOf(goalID)});
//        }
//    }

    public void removeGoal(int date){
        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s != %s and %s = %s",
                TABLE_FITNESS_TRACKER,
                GOAL_DATE,
                date,
                GOAL_DURATION,
                1
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int goalID = cursor.getInt(0);
            db.delete(TABLE_FITNESS_TRACKER, GOAL_ID + " = ?", new String[]{String.valueOf(goalID)});
        }
    }
    public void removeDailyGoal(int date){
        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s < %s and %s = %s",
                TABLE_FITNESS_TRACKER,
                GOAL_DATE,
                date,
                GOAL_DURATION,
                1
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int goalID = cursor.getInt(0);
            db.delete(TABLE_FITNESS_TRACKER, GOAL_ID + " = ?", new String[]{String.valueOf(goalID)});
        }
    }

    public void removeWeeklyGoal(int date){
        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s+7 = %s and %s = %s",
                TABLE_FITNESS_TRACKER,
                GOAL_DATE,
                date,
                GOAL_DURATION,
                7
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int goalID = cursor.getInt(0);
            db.delete(TABLE_FITNESS_TRACKER, GOAL_ID + " = ?", new String[]{String.valueOf(goalID)});
        }
    }

    // Adding new meal
    public boolean addPlan(MealPlanItem mpi)
    {
        // Search for duplicate date
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " = " + mpi.getDate() + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.d("In the DB: ", mpi.getDate()+"");
        // Only add if date does not exist
        if (!cursor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(MP_col_2_DATE, mpi.getDate());
            values.put(MP_col_3_RECIPEID, mpi.getRecipeID());
            values.put(MP_col_4_RECIPENAME, mpi.getRecipeName());
            // Inserting Row
            db.insert(TABLE_MEALPLAN, null, values);
            return true;
        }
        return false;
    }

    // Getting All Meal Plans
    public List<MealPlanItem> getAllPlans()
    {
        List<MealPlanItem> planList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " ORDER BY " + MP_col_2_DATE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MealPlanItem mpi = new MealPlanItem();
                mpi.setMpDate(cursor.getInt(1));
                mpi.setRecipe(cursor.getInt(2), cursor.getString(3));
                planList.add(mpi);
            } while (cursor.moveToNext());
        }
        return planList;
    }
    public MealPlanItem getPlan(int date)
    {
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " = " + date + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        MealPlanItem mpi = null;
        // Check if meal exists
        if (cursor.moveToFirst()) {
            do {
                mpi = new MealPlanItem();
                mpi.setMpDate(cursor.getInt(1));
                mpi.setRecipe(cursor.getInt(2), cursor.getString(3));
            } while (cursor.moveToNext());
        }
        return mpi;
    }

    public void removePlan(int date)
    {
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " = " + date + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int planID = cursor.getInt(0);
            db.delete(TABLE_MEALPLAN, MP_col_1_ID + " = ?", new String[]{String.valueOf(planID)});
        }
    }

    public boolean updatePlan(int oldDate, int newDate, int recipeID, String recipeName) {
        // Check if the new date is already in the database
        if (oldDate != newDate) {
            String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " = " + newDate + "";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return false;
            }
        }

        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " = " + oldDate + "";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MP_col_2_DATE, newDate);
        values.put(MP_col_3_RECIPEID, recipeID);
        values.put(MP_col_4_RECIPENAME, recipeName);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int planID = cursor.getInt(0);
            db.update(TABLE_MEALPLAN, values, MP_col_1_ID + " = ?", new String[]{String.valueOf(planID)});
        }

        return true;
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
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES +  " ORDER BY " + TR_col_2_NAME + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                recipeList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        return recipeList;

    }

    public boolean addIngredient(IngredientItem item)
    {
        // Search for duplicate ingredient
        String selectQuery = "SELECT * FROM " + TABLE_INGREDIENTS + " WHERE " + TI_col_2_NAME + " = '" + item.getName() + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Only add if ingredient does not exist
        if (!cursor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(TI_col_2_NAME, item.getName());
            values.put(TI_col_3_AMOUNT, item.getAmount());
            values.put(TI_col_4_MEASUREMENT, item.getMeasurement().toString());
            Log.d("STRING TYPE: ", item.getMeasurement().toString());
            // Inserting Row
            db.insert(TABLE_INGREDIENTS, null, values);

            return true;
        }
        return false;
    }

    public void removeIngredient()
    {

    }

    public ArrayList<IngredientItem> getAllIngredients()
    {
        ArrayList<IngredientItem> allIngredients = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_INGREDIENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IngredientItem item = new IngredientItem(cursor.getString(1), cursor.getFloat(2), cursor.getString(3), cursor.getInt(0));
                allIngredients.add(item);
            } while (cursor.moveToNext());
        }
        return allIngredients;
    }

    public void removeOldStuff(int oldDate){
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " <= " + oldDate + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int planID = cursor.getInt(0);
                db.delete(TABLE_MEALPLAN, MP_col_1_ID + " = ?", new String[]{String.valueOf(planID)});
            }while(cursor.moveToNext());
        }
//        removePlan(oldDate);
//        removeWeeklyGoal(oldDate);
//        removeGoal(oldDate);
    }


    protected void resetDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALPLAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FITNESS_TRACKER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(db);
    }
}