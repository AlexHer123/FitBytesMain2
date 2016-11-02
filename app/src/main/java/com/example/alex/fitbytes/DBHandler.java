package com.example.alex.fitbytes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 10/31/2016.
 */

public class DBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mealsPlanned";
    private static final String TABLE_MEALPLAN = "mealPlan";
    private static final String col_1_ID = "ID";
    private static final String col_2_DATE = "date";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEALPLAN_TABLE = "CREATE TABLE " + TABLE_MEALPLAN + "("
                + col_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_2_DATE + " TEXT" + ")";
        db.execSQL(CREATE_MEALPLAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALPLAN);
        onCreate(db);
    }

    // Adding new shop
    public void addPlan(String plan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col_2_DATE, plan);
        // Inserting Row
        db.insert(TABLE_MEALPLAN, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Shops
    public List<String> getAllPlans() {
        List<String> planList = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String tempDate = cursor.getString(1);
                planList.add(tempDate);
            } while (cursor.moveToNext());
        }
        return planList;
    }

    // Empty the table
    public void removeAll(){
        List<String> planList = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MEALPLAN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int planID = cursor.getInt(0);
                db.delete(TABLE_MEALPLAN, col_1_ID + " = ?", new String[] { String.valueOf(planID) });
            } while (cursor.moveToNext());
        }
    }

}
