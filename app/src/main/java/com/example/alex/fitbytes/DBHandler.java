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

public class DBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fitbytes.db";
    private static final String TABLE_MEALPLAN = "mealPlan";
    private static final String col_1_ID = "ID";
    private static final String col_2_DATE = "Date";
    private static final String col_3_RECIPE = "Recipe";

    private static final String TABLE_DATE = "currentDate";
    private static final String TD_col_1_DATE = "Date";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEALPLAN_TABLE = "CREATE TABLE " + TABLE_MEALPLAN + "("
                + col_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_2_DATE + " TEXT, " + col_3_RECIPE + " TEXT" + ")";
        db.execSQL(CREATE_MEALPLAN_TABLE);

        String CREATE_CURRENT_DATE_TABLE = "CREATE TABLE " + TABLE_DATE + " (" + TD_col_1_DATE + " INTEGER PRIMARY KEY)";
        db.execSQL(CREATE_CURRENT_DATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALPLAN);
        onCreate(db);
    }

    // Adding new meal
    public boolean addPlan(String date, String recipe) {
        // Search for duplicate date
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + col_2_DATE + " = '" + date + "'";
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


    // Getting All Shops
    public List<String> getAllPlans() {
        List<String> planList = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " ORDER BY " + col_2_DATE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //mealContainer temp = new mealContainer(cursor.getString(1), cursor.getString(2)); // cursor.getString(1);
                planList.add(cursor.getString(1) + " " + cursor.getString(2));
            } while (cursor.moveToNext());
        }

        return planList;
    }

    public void removePlan(String date){
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + col_2_DATE + " = '" + date + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int planID = cursor.getInt(0);
            db.delete(TABLE_MEALPLAN, col_1_ID + " = ?", new String[]{String.valueOf(planID)});
        }
    }

    public void addCurrentDate(int date){
        // Search for duplicate date
        String selectQuery = "SELECT * FROM " + TABLE_DATE;// + " WHERE " + col_2_DATE + " = '" + date + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Only add if date does not exist
        if (!cursor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(TD_col_1_DATE, date);
            // Inserting Row
            db.insert(TABLE_DATE, null, values);

        }

    }

    public List<Integer> getAllDates() {
        List<Integer> dateList = new ArrayList<Integer>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_DATE+ " ORDER BY " + TD_col_1_DATE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                dateList.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        return dateList;
    }

}
