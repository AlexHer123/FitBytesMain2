package com.example.alex.fitbytes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHandler db = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Creates a toolbar for MainActivity but throws the exception for other classes
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }catch (IllegalStateException e){}

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c.getTime());
        int date = Integer.parseInt(formattedDate);
        int oldDate = db.getCurrentDate();
        if (date != oldDate){
            // Put notifications here
            List<Goal> list = db.getExpiredGoal(oldDate);

            // THIS FOR LOOP JUST PRINTS OUT THE GOAL
//            for (Goal g : list){
//                Log.d("DLKFJL", g.toString());
//            }
            getDefaultGoals();
        }
        db.removeOldStuff(db.getCurrentDate());
        db.addCurrentDate(date);
        db.createUser();


        ////////////////////////////////////////////////////////////////////////////////
        if (db.hasMealToday()) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.mipmap.bread_notif);
            mBuilder.setContentTitle("FitBytes");
            mBuilder.setContentText("You have a meal today!");

            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationID allows you to update the notification later on.
            int notificationID = 001;
            mNotificationManager.notify(notificationID, mBuilder.build());
        }
        ////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////
        if (db.hasExpiredGoals()) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.mipmap.bread_notif);
            mBuilder.setContentTitle("FitBytes");
            mBuilder.setContentText("Some goal(s) has expired.");

            Intent resultIntent = new Intent(this, FitnessTracker.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(FitnessTracker.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationID allows you to update the notification later on.
            int notificationID_2 = 002;
            mNotificationManager.notify(notificationID_2, mBuilder.build());
        }
        ////////////////////////////////////////////////////////////////////////////////////

        createDefaultRecipes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_userProfile) {
            Intent intent = new Intent(this, UserProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_mealPlan) {
            Intent intent = new Intent(this, UpcomingPlans.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_recipe) {
            Intent intent = new Intent(this, Recipes.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_pantry) {
            Intent intent = new Intent(this, Pantry.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_fitnessTracker) {
            Intent intent = new Intent(this, FitnessTracker.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_clear_database) {
            db.resetDatabase();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDefaultGoals(){
//        int tempDate = db.getCurrentDate()-100;
//        Goal daily = db.getDailyGoal(tempDate);
//        if (daily == null){
            db.addGoal(new DailyGoal());
//        }

//        Goal weekly = db.getWeeklyGoal(tempDate);
//        if (weekly == null){
//            db.addGoal(new WeeklyGoal());
//        }
    }

    private void createDefaultRecipes(){
        int[] ids = {295841,547664,269358,15249,482760,710058,558627,573573,749303,715328};
        String[] names = {"Eggs Benedict","Baked Blueberry Coconut Oatmeal","Downtown Turkey Wrap","Kale Salad With Apricots, Avocado & Parmesan","Grilled Salmon with Lemon Garlic Sauce","Perfect Lasagna","Banana Nut Protein Bars","Very Berry Chocolate Protein Smoothie","Light Carrot Cake","The Best Apple Pie"};
        for (int i = 0; i<10; i++){
            db.addDefaultRecipe(ids[i], names[i]);
        }
    }
}
