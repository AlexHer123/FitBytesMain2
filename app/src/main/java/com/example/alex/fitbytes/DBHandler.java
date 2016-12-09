package com.example.alex.fitbytes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 10/31/2016.
 */

public class DBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fitbytes.db";

    private static final String TABLE_USER = "user";
    private static final String US_col_1_ID = "ID";
    private static final String US_col_2_NAME = "Name";
    private static final String US_col_3_HEIGHT = "Height";
    private static final String US_col_4_WEIGHT = "Weight";
    private static final String US_col_5_BMI = "BMI";

    private static final String TABLE_MEALPLAN = "mealPlan";
    private static final String MP_col_1_ID = "ID";
    private static final String MP_col_2_DATE = "Date";
    private static final String MP_col_3_RECIPEID1 = "RecipeID1";
    private static final String MP_col_4_RECIPENAME1 = "RecipeName1";
    private static final String MP_col_5_RECIPEID2 = "RecipeID2";
    private static final String MP_col_6_RECIPENAME2 = "RecipeName2";
    private static final String MP_col_7_RECIPEID3 = "RecipeID3";
    private static final String MP_col_8_RECIPENAME3 = "RecipeName3";

    private static final String TABLE_FITNESS_TRACKER = "fitnessTracker";
    private static final String GOAL_ID = "goal_id";
    private static final String GOAL_DESCRIPTION = "goal_description";
    private static final String GOAL_DATE = "goal_date";
    private static final String GOAL_DURATION = "goal_duration";
    private static final String GOAL_COMPLETED = "goal_completed";
    private static final String GOAL_TYPE = "goal_type";

    private static final String TABLE_DATE = "currentDate";
    private static final String TD_col_1_DATE = "Date";

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
        String CREATE_USER_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER, %s REAL)",
                TABLE_USER, US_col_1_ID, US_col_2_NAME, US_col_3_HEIGHT, US_col_4_WEIGHT, US_col_5_BMI);
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_MEALPLAN_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER DEFAULT 0, %s TEXT DEFAULT NULL, %s INTEGER DEFAULT 0, %s TEXT DEFAULT NULL)",
                TABLE_MEALPLAN, MP_col_1_ID, MP_col_2_DATE, MP_col_3_RECIPEID1, MP_col_4_RECIPENAME1, MP_col_5_RECIPEID2, MP_col_6_RECIPENAME2, MP_col_7_RECIPEID3, MP_col_8_RECIPENAME3);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FITNESS_TRACKER);
        onCreate(db);
    }

    public void createUser(){
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (!cursor.moveToFirst()) {
            values.put(US_col_2_NAME, "User");
            values.put(US_col_3_HEIGHT, 0);
            values.put(US_col_4_WEIGHT, 0);
            values.put(US_col_5_BMI, 0);
            db.insert(TABLE_USER, null, values);
        }
    }

    public UserItem getUser(){
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery(selectQuery, null);
        UserItem user = new UserItem("User", 0, 0, 0);;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(1);
            int height = cursor.getInt(2);
            int weight = cursor.getInt(3);
            double BMI = cursor.getDouble(4);
            user = new UserItem(name, height, weight, BMI);
        }
        return user;
    }

    public void updateUser(String name, int height, int weight, double BMI){

        String selectQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            values.put(US_col_2_NAME, name);
            values.put(US_col_3_HEIGHT, height);
            values.put(US_col_4_WEIGHT, weight);
            values.put(US_col_5_BMI, BMI);

            int planID = cursor.getInt(0);
            db.update(TABLE_USER, values, US_col_1_ID + " = ?", new String[]{String.valueOf(planID)});
        }
    }

    /*public Goal getGoal(Goal.Type type){
        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = '%s'", TABLE_FITNESS_TRACKER, GOAL_TYPE, type
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if(cursor == null){

        }
    }*/
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

    public void removeGoals(){
        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = %s",
                TABLE_FITNESS_TRACKER,
                GOAL_DURATION,
                getCurrentDate()
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int goalID = cursor.getInt(0);
            db.delete(TABLE_FITNESS_TRACKER, GOAL_ID + " = ?", new String[]{String.valueOf(goalID)});
        }
    }

    public void removeGoal(Goal goal){
        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = '%s'",
                TABLE_FITNESS_TRACKER,
                GOAL_DESCRIPTION, goal.getDescription()
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int goalID = cursor.getInt(0);
            db.delete(TABLE_FITNESS_TRACKER, GOAL_ID + " = ?", new String[]{String.valueOf(goalID)});
        }
    }

    public boolean addAllMeals(MealPlanItem mpi){
        // Search for duplicate date
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " = " + mpi.getDate() + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Only add if date does not exist
        if (!cursor.moveToFirst()){
            ContentValues values = new ContentValues();
            List<Map.Entry<Integer, String>> entries = new ArrayList(mpi.getRecipes().entrySet());
            int numMeals = entries.size();
            values.put(MP_col_2_DATE, mpi.getDate());
            switch (numMeals){
                case 3: values.put(MP_col_7_RECIPEID3, entries.get(2).getKey());
                        values.put(MP_col_8_RECIPENAME3, entries.get(2).getValue());
                case 2: values.put(MP_col_5_RECIPEID2, entries.get(1).getKey());
                        values.put(MP_col_6_RECIPENAME2, entries.get(1).getValue());
                default: values.put(MP_col_3_RECIPEID1, entries.get(0).getKey());
                         values.put(MP_col_4_RECIPENAME1, entries.get(0).getValue());
            }
            // Inserting Row
            db.insert(TABLE_MEALPLAN, null, values);
            return true;
        }
        return false;
    }

    public List<MealPlanItem> getAllMealPlans()
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
                for (int i = 2; i < 8; i+=2){

                    if(cursor.getInt(i) != 0){
                        mpi.addRecipe(cursor.getInt(i), cursor.getString(i+1));
                    }
                }
                planList.add(mpi);
            } while (cursor.moveToNext());
        }

        return planList;
    }

    public MealPlanItem getMealPlan(int date)
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
                for (int i = 2; i < 8; i+=2){

                    if(cursor.getInt(i) != 0){
                        mpi.addRecipe(cursor.getInt(i), cursor.getString(i+1));
                    }
                }
            } while (cursor.moveToNext());
        }
        return mpi;
    }

    public void removeMealPlan(int date)
    {
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " = " + date + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int planID = cursor.getInt(0);
            db.delete(TABLE_MEALPLAN, MP_col_1_ID + " = ?", new String[]{String.valueOf(planID)});
        }
    }

    public boolean updateMealPlan(int oldDate, int newDate, MealPlanItem mpi){
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

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            List<Map.Entry<Integer, String>> entries = new ArrayList(mpi.getRecipes().entrySet());
            int numMeals = entries.size();
            values.put(MP_col_2_DATE, newDate);
            if (numMeals > 0){
                values.put(MP_col_3_RECIPEID1, entries.get(0).getKey());
                values.put(MP_col_4_RECIPENAME1, entries.get(0).getValue());
                values.put(MP_col_5_RECIPEID2, 0);
                values.put(MP_col_6_RECIPENAME2, "NULL");
                values.put(MP_col_7_RECIPEID3, 0);
                values.put(MP_col_8_RECIPENAME3, "NULL");
            }
            if (numMeals > 1){
                values.put(MP_col_5_RECIPEID2, entries.get(1).getKey());
                values.put(MP_col_6_RECIPENAME2, entries.get(1).getValue());
            }
            if (numMeals > 2){
                values.put(MP_col_7_RECIPEID3, entries.get(2).getKey());
                values.put(MP_col_8_RECIPENAME3, entries.get(2).getValue());
            }

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
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " < " + oldDate + "";
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

    public void updateGoal(Goal updated, Goal current) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s SET %s = %s WHERE %s = '%s'",
                TABLE_FITNESS_TRACKER,
                GOAL_DURATION,
                updated.getDuration(),
                GOAL_DURATION,
                current.getDuration()));
        db.execSQL(String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'",
                TABLE_FITNESS_TRACKER,
                GOAL_DESCRIPTION,
                updated.getDescription(),
                GOAL_DESCRIPTION,
                current.getDescription()));

    }

    public void addGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GOAL_DESCRIPTION, goal.getDescription());
        values.put(GOAL_DATE, goal.getDate());
        values.put(GOAL_DURATION, goal.getDuration());
        values.put(GOAL_COMPLETED, goal.getCompleted());
        values.put(GOAL_TYPE, goal.getType().toString());
        db.insert(TABLE_FITNESS_TRACKER, null, values);
    }
}