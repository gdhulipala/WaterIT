package com.example.ganga.drinkwater.sync;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.ganga.drinkwater.data.WaterContract;
import com.example.ganga.drinkwater.data.WaterDbHelper;
import com.example.ganga.drinkwater.utilities.WaterCountNotificationUtilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ganga on 5/4/18.
 */

public class ReminderTasks {


    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static final String ACTION_DECREMENT_WATER_COUNT = "decrement-water-count";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_CHARGING_REMINDER = "charging-reminder";
    private static final int NOTI_PRIMARY1 = 1100;

    private final  String TAG = ReminderTasks.class.getSimpleName();


    public static void executeTask(Context context, String action) {
        if (ACTION_INCREMENT_WATER_COUNT.equals(action)) {
            incrementWaterCount(context);
        } else if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            WaterCountNotificationUtilities.clearAllNotifications(context);
        } else if (ACTION_DECREMENT_WATER_COUNT.equals(action)) {

            decrementWaterCount(context);

        } else {


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

                WaterCountNotificationUtilities.remindUserBecauseCharging(context);
                return;


            }


            NotificationCompat.Builder nb = null;

            NotificationHelper help = new NotificationHelper(context);
            nb = help.getNotification1("WaterIT", "Have You Had Your Cup Of Water?");
            help.notify(NOTI_PRIMARY1, nb);


            //help.getNotification2();


        }
    }


    synchronized public static void incrementWaterCount(Context context) {

        String dateText = "0";
        String timeStamp = "1";
        WaterDbHelper mDbHelper;

        int currentNumberDrinks=0;

        String date ="0";

        long idColumn=1;

        ContentResolver sunshineContentResolver = context.getContentResolver();


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy/HH:mm:ss", Locale.US);
        String strDate = sdf.format(cal.getTime());
        String[] values = strDate.split("/", 0);

        for (int i = 0; i < values.length; i++) {

            dateText = (values[0]);
            timeStamp = (values[1]);
        }

        mDbHelper = new WaterDbHelper(context);

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {WaterContract.WaterEntry._ID,
                WaterContract.WaterEntry.COLUMN_TOTALCUPS,
                WaterContract.WaterEntry.COLUMN_DATE};

        Cursor cursor = database.query(WaterContract.WaterEntry.TABLE_NAME, projection, null, null,
                null, null, null);

        if(cursor.getCount()>4){

            // Get writeable database
            SQLiteDatabase writabledatabase = mDbHelper.getWritableDatabase();

            cursor.moveToFirst();
            int ids = cursor.getColumnIndex(WaterContract.WaterEntry._ID);
            idColumn = cursor.getLong(ids);

            Uri currentPetUri = ContentUris.withAppendedId(WaterContract.WaterEntry.CONTENT_URI, idColumn);

            int rowsDeleted = sunshineContentResolver.delete(currentPetUri, null, null);


        }

        if (cursor.moveToLast()) {

            int cupColumnIndex = cursor.getColumnIndex(WaterContract.WaterEntry.COLUMN_DATE);
            date = cursor.getString(cupColumnIndex);
            int ids = cursor.getColumnIndex(WaterContract.WaterEntry._ID);
            idColumn = cursor.getLong(ids);
            int numberOfCupsIndex = cursor.getColumnIndex(WaterContract.WaterEntry.COLUMN_TOTALCUPS);
            int numberOfCups = cursor.getInt(numberOfCupsIndex);
            currentNumberDrinks = numberOfCups + 1;
            cursor.close();

        }

        if (date.equals(dateText)) {


            ContentValues contentvalues = new ContentValues();
            contentvalues.put(WaterContract.WaterEntry.COLUMN_DATE, dateText);
            contentvalues.put(WaterContract.WaterEntry.COLUMN_TIME, timeStamp);
            contentvalues.put(WaterContract.WaterEntry.COLUMN_TOTALCUPS, currentNumberDrinks);

            Uri currentPetUri = ContentUris.withAppendedId(WaterContract.WaterEntry.CONTENT_URI, idColumn);
            int rowsAffected = sunshineContentResolver.update(currentPetUri, contentvalues, null, null);
            WaterCountNotificationUtilities.clearAllNotifications(context);


        } else {
            currentNumberDrinks=1;
            ContentValues contentvalues = new ContentValues();
            contentvalues.put(WaterContract.WaterEntry.COLUMN_DATE, dateText);
            contentvalues.put(WaterContract.WaterEntry.COLUMN_TIME, timeStamp);
            contentvalues.put(WaterContract.WaterEntry.COLUMN_TOTALCUPS, currentNumberDrinks);

            // Get writeable database
            SQLiteDatabase writabledatabase = mDbHelper.getWritableDatabase();

            Uri newUri = sunshineContentResolver.insert(WaterContract.WaterEntry.CONTENT_URI, contentvalues);
            WaterCountNotificationUtilities.clearAllNotifications(context);

        }


    }

    public static void decrementWaterCount(Context context){

        String dateText = "0";
        String timeStamp = "1";
        WaterDbHelper mDbHelper;

        int currentNumberDrinks=0;

        String date ="0";

        long idColumn=1;

        ContentResolver sunshineContentResolver = context.getContentResolver();


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy/HH:mm:ss", Locale.US);
        String strDate = sdf.format(cal.getTime());
        String[] values = strDate.split("/", 0);

        for (int i = 0; i < values.length; i++) {

            dateText = (values[0]);
            timeStamp = (values[1]);
        }

        mDbHelper = new WaterDbHelper(context);

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {WaterContract.WaterEntry._ID,
                WaterContract.WaterEntry.COLUMN_TOTALCUPS,
                WaterContract.WaterEntry.COLUMN_DATE};

        Cursor cursor = database.query(WaterContract.WaterEntry.TABLE_NAME, projection, null, null,
                null, null, null);

        if(cursor.getCount()>4){

            // Get writeable database
            SQLiteDatabase writabledatabase = mDbHelper.getWritableDatabase();

            cursor.moveToFirst();
            int ids = cursor.getColumnIndex(WaterContract.WaterEntry._ID);
            idColumn = cursor.getLong(ids);

            Uri currentPetUri = ContentUris.withAppendedId(WaterContract.WaterEntry.CONTENT_URI, idColumn);

            int rowsDeleted = sunshineContentResolver.delete(currentPetUri, null, null);


        }

        if (cursor.moveToLast()) {

            int cupColumnIndex = cursor.getColumnIndex(WaterContract.WaterEntry.COLUMN_DATE);
            date = cursor.getString(cupColumnIndex);
            int ids = cursor.getColumnIndex(WaterContract.WaterEntry._ID);
            idColumn = cursor.getLong(ids);
            int numberOfCupsIndex = cursor.getColumnIndex(WaterContract.WaterEntry.COLUMN_TOTALCUPS);
            int numberOfCups = cursor.getInt(numberOfCupsIndex);
            if(numberOfCups>0) {
                currentNumberDrinks = numberOfCups - 1;
            } else{

                currentNumberDrinks=numberOfCups;

            }
            cursor.close();

        }

        if (date.equals(dateText)) {


            ContentValues contentvalues = new ContentValues();
            contentvalues.put(WaterContract.WaterEntry.COLUMN_DATE, dateText);
            contentvalues.put(WaterContract.WaterEntry.COLUMN_TIME, timeStamp);
            contentvalues.put(WaterContract.WaterEntry.COLUMN_TOTALCUPS, currentNumberDrinks);

            Uri currentPetUri = ContentUris.withAppendedId(WaterContract.WaterEntry.CONTENT_URI, idColumn);
            int rowsAffected = sunshineContentResolver.update(currentPetUri, contentvalues, null, null);
            WaterCountNotificationUtilities.clearAllNotifications(context);


        }else{


            currentNumberDrinks=0;
            ContentValues contentvalues = new ContentValues();
            contentvalues.put(WaterContract.WaterEntry.COLUMN_DATE, dateText);
            contentvalues.put(WaterContract.WaterEntry.COLUMN_TIME, timeStamp);
            contentvalues.put(WaterContract.WaterEntry.COLUMN_TOTALCUPS, currentNumberDrinks);

            // Get writeable database
            SQLiteDatabase writabledatabase = mDbHelper.getWritableDatabase();

            Uri newUri = sunshineContentResolver.insert(WaterContract.WaterEntry.CONTENT_URI, contentvalues);
            WaterCountNotificationUtilities.clearAllNotifications(context);

            Toast.makeText(context, "You Haven't Has A Drink Yet", Toast.LENGTH_SHORT).show();



        }
    }
}