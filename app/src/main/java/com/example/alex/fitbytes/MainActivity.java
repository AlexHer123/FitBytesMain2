package com.example.alex.fitbytes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TOGGLE_NOTIFICATION = "yes";
    public static final String NOTIFICATION = "no";
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
        checkIfNotificationIsHidden();
        /*if(!notificationCanBeDisplayed){
            ((MenuItem) findViewById(R.id.action_settings)).setTitle("Turn Notifications On");
        } else {
            ((MenuItem) findViewById(R.id.action_settings)).setTitle("Turn Notifications Off");
        }*/
        /*if (date != oldDate && notificationCanBeDisplayed){
            // Put notifications here
            if (db.hasMealToday() || db.hasExpiredGoals()) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                mBuilder.setSmallIcon(R.mipmap.bread_notif);
                mBuilder.setContentTitle("FitBytes");
                mBuilder.setContentText("You have a something today!");

                Intent resultIntent = new Intent(this, UserProfile.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(MainActivity.class);

                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // notificationID allows you to update the notificationCanBeDisplayed later on.
                int notificationID = 001;
                mNotificationManager.notify(notificationID, mBuilder.build());
            }

            List<Goal> list = db.getExpiredGoal(oldDate);

            // THIS FOR LOOP JUST PRINTS OUT THE GOAL
//            for (Goal g : list){
//                Log.d("DLKFJL", g.toString());
//            }

        }*/
        /*db.removeOldStuff(db.getCurrentDate());
        db.addCurrentDate(date);
        db.createUser();
        getDefaultGoals();*/

        ///////////////////////////////////////////////////////////////////////////////

        db.removeOldStuff(db.getCurrentDate());
        db.addCurrentDate(date);
        db.createUser();
        getDefaultGoals();


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
//            Intent intent = new Intent(this, UserRecipe.class);
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
            /*Intent intent = new Intent(this, Settings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
            toggleNotifications();
            return true;
        }
/*        if (id == R.id.action_clear_database) {
            db.resetDatabase();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void getDefaultGoals(){
        if(db.getAllGoals().isEmpty()){
            Goal[] g = {new DailyGoal(), new WeeklyGoal()};
            for(Goal goal : g) db.addGoal(goal);
        }
    }

    private void createDefaultRecipes(){
        int[] ids = {295841,547664,269358,15249,482760,710058,558627,573573,749303,715328};
        String[] names = {"Eggs Benedict","Baked Blueberry Coconut Oatmeal","Downtown Turkey Wrap","Kale Salad With Apricots, Avocado & Parmesan","Grilled Salmon with Lemon Garlic Sauce","Perfect Lasagna","Banana Nut Protein Bars","Very Berry Chocolate Protein Smoothie","Light Carrot Cake","The Best Apple Pie"};
        for (int i = 0; i<10; i++){
            db.addDefaultRecipe(ids[i], names[i]);
        }
    }

    private static boolean notificationCanBeDisplayed;
    private void toggleNotifications(){
        SharedPreferences pref = getSharedPreferences(TOGGLE_NOTIFICATION, Context.MODE_PRIVATE);
        notificationCanBeDisplayed = !notificationCanBeDisplayed;
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(NOTIFICATION, notificationCanBeDisplayed);
        editor.commit();
        Toast.makeText(this, "Notifications are now turned " + (notificationCanBeDisplayed ? "on" : "off"), Toast.LENGTH_LONG).show();
    }
    private void checkIfNotificationIsHidden(){
        SharedPreferences pref = getSharedPreferences(TOGGLE_NOTIFICATION, Context.MODE_PRIVATE);
        notificationCanBeDisplayed = pref.getBoolean(NOTIFICATION, true);
        //Toast.makeText(this, "Loaded " + notificationCanBeDisplayed, Toast.LENGTH_SHORT).show();
    }
}
