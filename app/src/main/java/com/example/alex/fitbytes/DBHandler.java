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

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fitbytes.db";

    private static final String TABLE_USER = "user";
    private static final String US_col_1_ID = "ID";
    private static final String US_col_2_NAME = "Name";
    private static final String US_col_3_HEIGHT = "Height";
    private static final String US_col_4_WEIGHT = "Weight";
    private static final String US_col_5_BMI = "BMI";
    private static final String US_col_6_CALORIES = "Calories";
    private static final String US_col_7_FAT = "Fat";
    private static final String US_col_8_CARBS = "Carbs";
    private static final String US_col_9_SUGAR = "Sugar";
    private static final String US_col_10_CHOLESTEROL = "Cholesterol";
    private static final String US_col_11_SODIUM = "Sodium";
    private static final String US_col_12_PROTEIN = "Protein";
    private static final String US_col_13_TOTALMEALS = "TotalMeals";

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

    private static final String TABLE_DEFAULT_RECIPES = "defaultRecipe";
    private static final String TR_col_1_ID = "ID";
    private static final String TR_col_2_APIID = "apiID";
    private static final String TR_col_3_NAME = "name";

    private static final String TABLE_USER_RECIPE = "userRecipe";
    private static final String UR_col_1_ID = "ID";
    private static final String UR_col_2_NAME = "name";
    private static final String UR_col_3_SERVING = "serving";
    private static final String UR_col_4_READYTIME = "readyTime";
    private static final String UR_col_5_CALORIE = "calorie";
    private static final String UR_col_6_FAT = "fat";
    private static final String UR_col_7_CARB = "carb";
    private static final String UR_col_8_SUGAR = "sugar";
    private static final String UR_col_9_CHOLE = "chole";
    private static final String UR_col_10_SODIUM = "sodium";
    private static final String UR_col_11_PROTEIN = "protein";
    private static final String UR_col_12_INGREDIENT = "ingredient";
    private static final String UR_col_13_DIRECTION = "direction";
    private static final String UR_col_14_ABOUT = "about";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER, %s REAL, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER)",
                TABLE_USER, US_col_1_ID, US_col_2_NAME, US_col_3_HEIGHT, US_col_4_WEIGHT, US_col_5_BMI, US_col_6_CALORIES, US_col_7_FAT, US_col_8_CARBS, US_col_9_SUGAR, US_col_10_CHOLESTEROL, US_col_11_SODIUM, US_col_12_PROTEIN, US_col_13_TOTALMEALS
        );
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_MEALPLAN_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER DEFAULT 0, %s TEXT DEFAULT NULL, %s INTEGER DEFAULT 0, %s TEXT DEFAULT NULL)",
                TABLE_MEALPLAN, MP_col_1_ID, MP_col_2_DATE, MP_col_3_RECIPEID1, MP_col_4_RECIPENAME1, MP_col_5_RECIPEID2, MP_col_6_RECIPENAME2, MP_col_7_RECIPEID3, MP_col_8_RECIPENAME3);
        db.execSQL(CREATE_MEALPLAN_TABLE);

        String CREATE_CURRENT_DATE_TABLE = "CREATE TABLE " + TABLE_DATE + " (" + TD_col_1_DATE + " INTEGER PRIMARY KEY)";
        db.execSQL(CREATE_CURRENT_DATE_TABLE);

        String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_DEFAULT_RECIPES + "("
                + TR_col_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TR_col_2_APIID + " INTEGER, " + TR_col_3_NAME + " TEXT" + ")";
        db.execSQL(CREATE_RECIPE_TABLE);

        String CREATE_USER_RECIPE_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %S TEXT )",
                TABLE_USER_RECIPE, UR_col_1_ID, UR_col_2_NAME, UR_col_3_SERVING, UR_col_4_READYTIME, UR_col_5_CALORIE, UR_col_6_FAT, UR_col_7_CARB, UR_col_8_SUGAR, UR_col_9_CHOLE, UR_col_10_SODIUM, UR_col_11_PROTEIN, UR_col_12_INGREDIENT, UR_col_13_DIRECTION, UR_col_14_ABOUT
        );
        db.execSQL(CREATE_USER_RECIPE_TABLE);

        String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + "("
                + TI_col_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TI_col_2_NAME + " TEXT, " + TI_col_3_AMOUNT + " REAL, " + TI_col_4_MEASUREMENT + " TEXT" + ")";
        db.execSQL(CREATE_INGREDIENTS_TABLE);

        String CREATE_FITNESS_TRACKER_TABLE =
                String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER, %s BOOLEAN, %s TEXT)",
                        TABLE_FITNESS_TRACKER, GOAL_ID, GOAL_DESCRIPTION, GOAL_DATE, GOAL_DURATION, GOAL_COMPLETED, GOAL_TYPE);
        db.execSQL(CREATE_FITNESS_TRACKER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FITNESS_TRACKER);
        onCreate(db);
    }

    public void createUser() {
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (!cursor.moveToFirst()) {
            values.put(US_col_2_NAME, "User");
            values.put(US_col_3_HEIGHT, 0);
            values.put(US_col_4_WEIGHT, 0);
            values.put(US_col_5_BMI, 0);
            values.put(US_col_6_CALORIES, 0);
            values.put(US_col_7_FAT, 0);
            values.put(US_col_8_CARBS, 0);
            values.put(US_col_9_SUGAR, 0);
            values.put(US_col_10_CHOLESTEROL, 0);
            values.put(US_col_11_SODIUM, 0);
            values.put(US_col_12_PROTEIN, 0);
            values.put(US_col_13_TOTALMEALS, 0);

            db.insert(TABLE_USER, null, values);
        }
    }

    public UserItem getUser() {
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery(selectQuery, null);
        UserItem user = new UserItem("User", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(1);
            int height = cursor.getInt(2);
            int weight = cursor.getInt(3);
            double BMI = cursor.getDouble(4);

            int cals = cursor.getInt(5);
            int fat = cursor.getInt(6);
            int carbs = cursor.getInt(7);
            int sugar = cursor.getInt(8);
            int chol = cursor.getInt(9);
            int sodium = cursor.getInt(10);
            int protein = cursor.getInt(11);
            int totalMeals = cursor.getInt(12);

            user = new UserItem(name, height, weight, BMI, cals, fat, carbs, sugar, chol, sodium, protein, totalMeals);
        }
        return user;
    }

    public void updateUser(String name, int height, int weight, double BMI) {

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

    public void updateUserNutrients(int cal, int fat, int carbs, int sugar, int chol, int sodium, int protein, int totalMeals) {
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            values.put(US_col_6_CALORIES, cal);
            values.put(US_col_7_FAT, fat);
            values.put(US_col_8_CARBS, carbs);
            values.put(US_col_9_SUGAR, sugar);
            values.put(US_col_10_CHOLESTEROL, chol);
            values.put(US_col_11_SODIUM, sodium);
            values.put(US_col_12_PROTEIN, protein);
            values.put(US_col_13_TOTALMEALS, totalMeals);

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

    public Goal getGoal(String name) {
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
        switch (cursor.getString(5)) {
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

    public void setGoalCompleted(Goal goal, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s SET %s = %s WHERE %s = '%s'",
                TABLE_FITNESS_TRACKER,
                GOAL_COMPLETED,
                completed ? 1 : 0,
                GOAL_ID,
                goal.getID()));
    }

    public List<Goal> getExpiredGoal(int oldDate) {
        List<Goal> list = new ArrayList<>();
        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s <= " + (oldDate-100) + " AND %s = 0",
                TABLE_FITNESS_TRACKER,
                GOAL_DURATION,
                GOAL_COMPLETED
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String description = cursor.getString(1);
                int date = cursor.getInt(2);
                int duration = cursor.getInt(3);
                boolean completed = cursor.getInt(4) > 0;
                Goal g = null;
                switch (cursor.getString(5)) {
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
                g.setID(cursor.getInt(0));
                list.add(g);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Goal> getAllGoals() {
        List<Goal> list = new ArrayList<>();
        String selectQuery = String.format(
                "SELECT * FROM %s ORDER BY %s ASC",
                TABLE_FITNESS_TRACKER,
                GOAL_DATE
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String description = cursor.getString(1);
                int date = cursor.getInt(2);
                int duration = cursor.getInt(3);
                boolean completed = cursor.getInt(4) > 0;
                Goal g = null;
                switch (cursor.getString(5)) {
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
                g.setID(cursor.getInt(0));
                list.add(g);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Goal> getAllGoals(int dur) {
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
        if (cursor.moveToFirst()) {
            do {
                String description = cursor.getString(1);
                int date = cursor.getInt(2);
                int duration = cursor.getInt(3);
                boolean completed = cursor.getInt(4) > 0;
                Goal g = null;
                switch (cursor.getString(5)) {
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
                g.setID(cursor.getInt(0));
                list.add(g);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Goal> getActiveGoals(int currentDate) {
        List<Goal> list = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String selectWeekly = String.format(
                "SELECT * FROM %s WHERE %s = '%s'",
                TABLE_FITNESS_TRACKER,
                GOAL_TYPE,
                Goal.Type.WEEKLY.toString()

        );

        Cursor cursor = db.rawQuery(selectWeekly, null);
        if (cursor.moveToFirst()) {
            Goal g = getGoalFromCursor(cursor);
            list.add(g);
        }

        String selectDaily = String.format(
                "SELECT * FROM %s WHERE %s = '%s'",
                TABLE_FITNESS_TRACKER,
                GOAL_TYPE,
                Goal.Type.DAILY.toString()
        );

        cursor = db.rawQuery(selectDaily, null);
        if (cursor.moveToFirst()) {
            Goal g = getGoalFromCursor(cursor);
            list.add(g);
        }

        String selectUser = String.format(
                "SELECT * FROM %s WHERE %s = '%s' ORDER BY %s ASC",
                TABLE_FITNESS_TRACKER,
                GOAL_TYPE,
                Goal.Type.USER.toString(),
                GOAL_DURATION
        );

        cursor = db.rawQuery(selectUser, null);
        if (cursor.moveToFirst()) {
            int count = 0;
            do {
                Goal g = getGoalFromCursor(cursor);
                list.add(g);
                count++;
            } while (cursor.moveToNext() && count < 3);
        }

        return list;
    }

    private Goal getGoalFromCursor(Cursor cursor) {
        Goal g = null;
        String description = cursor.getString(1);
        int date = cursor.getInt(2);
        int duration = cursor.getInt(3);
        boolean completed = cursor.getInt(4) > 0;
        switch (cursor.getString(5)) {
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
        return g;
    }

    public void removeGoals() {
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

    public void removeGoal(Goal goal) {
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

    public boolean addAllMeals(MealPlanItem mpi) {
        // Search for duplicate date
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " = " + mpi.getDate() + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Only add if date does not exist
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            List<Map.Entry<Integer, String>> entries = new ArrayList(mpi.getRecipes().entrySet());
            int numMeals = entries.size();
            values.put(MP_col_2_DATE, mpi.getDate());
            switch (numMeals) {
                case 3:
                    values.put(MP_col_7_RECIPEID3, entries.get(2).getKey());
                    values.put(MP_col_8_RECIPENAME3, entries.get(2).getValue());
                case 2:
                    values.put(MP_col_5_RECIPEID2, entries.get(1).getKey());
                    values.put(MP_col_6_RECIPENAME2, entries.get(1).getValue());
                default:
                    values.put(MP_col_3_RECIPEID1, entries.get(0).getKey());
                    values.put(MP_col_4_RECIPENAME1, entries.get(0).getValue());
            }
            // Inserting Row
            db.insert(TABLE_MEALPLAN, null, values);
            return true;
        }
        return false;
    }

    public List<MealPlanItem> getAllMealPlans() {
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
                for (int i = 2; i < 8; i += 2) {

                    if (cursor.getInt(i) != 0) {
                        mpi.addRecipe(cursor.getInt(i), cursor.getString(i + 1));
                    }
                }
                planList.add(mpi);
            } while (cursor.moveToNext());
        }

        return planList;
    }

    public MealPlanItem getMealPlan(int date) {
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
                for (int i = 2; i < 8; i += 2) {

                    if (cursor.getInt(i) != 0) {
                        mpi.addRecipe(cursor.getInt(i), cursor.getString(i + 1));
                    }
                }
            } while (cursor.moveToNext());
        }
        return mpi;
    }

    public void removeMealPlan(int date) {
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " = " + date + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int planID = cursor.getInt(0);
            db.delete(TABLE_MEALPLAN, MP_col_1_ID + " = ?", new String[]{String.valueOf(planID)});
        }
    }

    public boolean hasMealToday() {
        SQLiteDatabase db = this.getWritableDatabase();
        int today = getCurrentDate();

        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " = " + today + "";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
            return false;
        else return true;
    }

    public boolean hasExpiredGoals() {
        SQLiteDatabase db = this.getWritableDatabase();
        int today = getCurrentDate();

        String selectQuery = String.format("SELECT * FROM %s WHERE %s %s %s AND %s %s %s", TABLE_FITNESS_TRACKER, GOAL_DURATION, "<", today, GOAL_COMPLETED, "=", 0);

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
            return false;
        else return true;
    }


    public boolean updateMealPlan(int oldDate, int newDate, MealPlanItem mpi) {
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
            if (numMeals > 0) {
                values.put(MP_col_3_RECIPEID1, entries.get(0).getKey());
                values.put(MP_col_4_RECIPENAME1, entries.get(0).getValue());
                values.put(MP_col_5_RECIPEID2, 0);
                values.put(MP_col_6_RECIPENAME2, "NULL");
                values.put(MP_col_7_RECIPEID3, 0);
                values.put(MP_col_8_RECIPENAME3, "NULL");
            }
            if (numMeals > 1) {
                values.put(MP_col_5_RECIPEID2, entries.get(1).getKey());
                values.put(MP_col_6_RECIPENAME2, entries.get(1).getValue());
            }
            if (numMeals > 2) {
                values.put(MP_col_7_RECIPEID3, entries.get(2).getKey());
                values.put(MP_col_8_RECIPENAME3, entries.get(2).getValue());
            }

            int planID = cursor.getInt(0);
            db.update(TABLE_MEALPLAN, values, MP_col_1_ID + " = ?", new String[]{String.valueOf(planID)});
        }

        return true;
    }

    public void addCurrentDate(int date) {
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

    public int getCurrentDate() {
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

    public boolean addDefaultRecipe(int id, String name) {
        // Search for duplicate recipe
        String selectQuery = "SELECT * FROM " + TABLE_DEFAULT_RECIPES + " WHERE " + TR_col_2_APIID + " = " + id + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Only add if recipe does not exist
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(TR_col_2_APIID, id);
            values.put(TR_col_3_NAME, name);
            // Inserting Row
            db.insert(TABLE_DEFAULT_RECIPES, null, values);

            return true;
        }
        return false;
    }

//    public String getRecipe(String name) {
//        // Search for recipe
//        String selectQuery = "SELECT * FROM " + TABLE_RECIPES + " WHERE " + TR_col_2_NAME + " = '" + name + "'";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            return cursor.getString(1);
//        }
//        return "NONE";
//    }

    public List<RecipeItem> getDefaultRecipes() {
        List<RecipeItem> recipeList = new ArrayList();
        String selectQuery = "SELECT * FROM " + TABLE_DEFAULT_RECIPES + " ORDER BY " + TR_col_2_APIID + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                recipeList.add(new RecipeItem(cursor.getInt(1), cursor.getString(2), 0));
            } while (cursor.moveToNext());
        }
        return recipeList;
    }

    public List<RecipeItem> getSelectedDefaultRecipes(String query) {
        List<RecipeItem> recipeList = new ArrayList();
        String selectQuery = "SELECT * FROM " + TABLE_DEFAULT_RECIPES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(2).toLowerCase().contains(query.toLowerCase())) {
                    recipeList.add(new RecipeItem(cursor.getInt(1), cursor.getString(2), 0));
                }
            } while (cursor.moveToNext());
        }
        return recipeList;
    }


    public void addUserRecipe(UserRecipeItem userRecipe) {
        String selectQuery = "SELECT * FROM " + TABLE_USER_RECIPE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ContentValues values = new ContentValues();


        values.put(UR_col_2_NAME, userRecipe.getName());
        values.put(UR_col_3_SERVING, userRecipe.getServing());
        values.put(UR_col_4_READYTIME, userRecipe.getReadyMin());
        values.put(UR_col_5_CALORIE, userRecipe.getCalorie());
        values.put(UR_col_6_FAT, userRecipe.getFat());
        values.put(UR_col_7_CARB, userRecipe.getCarbs());
        values.put(UR_col_8_SUGAR, userRecipe.getSugar());
        values.put(UR_col_9_CHOLE, userRecipe.getChol());
        values.put(UR_col_10_SODIUM, userRecipe.getSodium());
        values.put(UR_col_11_PROTEIN, userRecipe.getProtein());
        values.put(UR_col_12_INGREDIENT, userRecipe.getIngredients());
        values.put(UR_col_13_DIRECTION, userRecipe.getDirections());
        values.put(UR_col_14_ABOUT, userRecipe.getAboutRecipe());
        db.insert(TABLE_USER_RECIPE, null, values);
    }

    public UserRecipeItem getUserRecipe(int ID) {
        String selectQuery = "SELECT * FROM " + TABLE_USER_RECIPE + " WHERE " + UR_col_1_ID + " = " + ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ContentValues values = new ContentValues();

        UserRecipeItem temp = new UserRecipeItem("None");
        if (cursor.moveToFirst()) {
            temp.setServing(cursor.getInt(2));
            temp.setReadyMin(cursor.getInt(3));
            temp.setCalorie(cursor.getInt(4));
            temp.setFat(cursor.getInt(5));
            temp.setCarbs(cursor.getInt(6));
            temp.setSugar(cursor.getInt(7));
            temp.setChol(cursor.getInt(8));
            temp.setSodium(cursor.getInt(9));
            temp.setProtein(cursor.getInt(10));
            temp.setIngredients(cursor.getString(11));
            temp.setDirections(cursor.getString(12));
            temp.setAboutRecipe(cursor.getString(13));
        }

        return temp;
    }

    public List<UserRecipeItem> getAllUserRecipe(String query) {
        List<UserRecipeItem> recipeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER_RECIPE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ContentValues values = new ContentValues();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String ingredient = cursor.getString(11);
                if (name.toLowerCase().contains(query.toLowerCase()) || ingredient.toLowerCase().contains(query.toLowerCase())) {
                    UserRecipeItem temp = new UserRecipeItem(name);
                    temp.setID(cursor.getInt(0));
                    temp.setServing(cursor.getInt(2));
                    temp.setReadyMin(cursor.getInt(3));
                    temp.setCalorie(cursor.getInt(4));
                    temp.setFat(cursor.getInt(5));
                    temp.setCarbs(cursor.getInt(6));
                    temp.setSugar(cursor.getInt(7));
                    temp.setChol(cursor.getInt(8));
                    temp.setSodium(cursor.getInt(9));
                    temp.setProtein(cursor.getInt(10));
                    temp.setIngredients(cursor.getString(11));
                    temp.setDirections(cursor.getString(12));
                    temp.setAboutRecipe(cursor.getString(13));
                    recipeList.add(temp);
                }
            } while (cursor.moveToNext());
        }

        return recipeList;
    }

    public boolean addIngredient(IngredientItem item) {
        // Search for duplicate ingredient
        String selectQuery = "SELECT * FROM " + TABLE_INGREDIENTS + " WHERE " + TI_col_2_NAME + " = '" + item.getName() + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ContentValues values = new ContentValues();

        // Only add if ingredient does not exist
        if (!cursor.moveToFirst()) {
            values.put(TI_col_2_NAME, item.getName());
            values.put(TI_col_3_AMOUNT, item.getQuantity());
            values.put(TI_col_4_MEASUREMENT, item.getMeasurement());
            db.insert(TABLE_INGREDIENTS, null, values);

            return true;
        } else {
            int currentAmount = cursor.getInt(2);
            item.setQuantity(item.getQuantity() + currentAmount);
            updatePantryIngredient(item);
            return true;
        }
    }

    public void removeIngredient(String name) {
        String selectQuery = "SELECT * FROM " + TABLE_INGREDIENTS + " WHERE " + TI_col_2_NAME + " = '" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int planID = cursor.getInt(0);
            db.delete(TABLE_INGREDIENTS, TI_col_1_ID + " = ?", new String[]{String.valueOf(planID)});
        }
    }

    public void updatePantryIngredient(IngredientItem item) {
        String selectQuery = "SELECT * FROM " + TABLE_INGREDIENTS + " WHERE " + TI_col_2_NAME + " = '" + item.getName() + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ContentValues values = new ContentValues();

        if (cursor.moveToFirst()) {
            int currentAmount = cursor.getInt(2);
            values.put(TI_col_3_AMOUNT, item.getQuantity());
            int planID = cursor.getInt(0);
            db.update(TABLE_INGREDIENTS, values, TI_col_1_ID + " = ?", new String[]{String.valueOf(planID)});
        }
    }

    public List<IngredientItem> getAllIngredients() {
        List<IngredientItem> allIngredients = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_INGREDIENTS + " ORDER BY " + TI_col_2_NAME + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IngredientItem item = new IngredientItem(cursor.getString(1), cursor.getInt(2), cursor.getString(3));
                allIngredients.add(item);
            } while (cursor.moveToNext());
        }
        return allIngredients;
    }

    public void removeOldStuff(int oldDate) {
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + MP_col_2_DATE + " < " + oldDate + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int planID = cursor.getInt(0);
                db.delete(TABLE_MEALPLAN, MP_col_1_ID + " = ?", new String[]{String.valueOf(planID)});
            } while (cursor.moveToNext());
        }
    }

    protected void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALPLAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FITNESS_TRACKER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEFAULT_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(db);
    }

    public void updateGoal(Goal updated, Goal current) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s SET %s = %s WHERE %s = '%s'",
                TABLE_FITNESS_TRACKER,
                GOAL_DURATION,
                updated.getDueDate(),
                GOAL_DURATION,
                current.getDueDate()));
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
        values.put(GOAL_DURATION, goal.getDueDate());
        values.put(GOAL_COMPLETED, goal.getCompleted());
        values.put(GOAL_TYPE, goal.getType().toString());
        db.insert(TABLE_FITNESS_TRACKER, null, values);
    }
}