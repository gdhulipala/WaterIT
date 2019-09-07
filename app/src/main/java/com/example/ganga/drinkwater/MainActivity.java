package com.example.ganga.drinkwater;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ganga.drinkwater.data.WaterContract;
import com.example.ganga.drinkwater.data.WaterDbHelper;
import com.example.ganga.drinkwater.sync.ReminderTasks;
import com.example.ganga.drinkwater.sync.ReminderUtilities;
import com.example.ganga.drinkwater.sync.WaterReminderIntentService;
import com.example.ganga.drinkwater.utilities.WaterCountNotificationUtilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = MainActivity.class.getSimpleName();

    private WaterDbHelper mDbHelper;

    TextView drinkCounts;

    int currentNumberDrinks;

    int numberOfCups;

    String date;

    long idColumn;

    String dateText;

    String timeStamp;

    /**
     * Identifier for the pet data loader
     */
    private static final int PET_LOADER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WaterCountNotificationUtilities.clearAllNotifications(this);

        ReminderUtilities.scheduleChargingReminder(this);


        dateText = "0";
        timeStamp = "1";


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy/HH:mm:ss", Locale.US);
        String strDate = sdf.format(cal.getTime());
        String[] values = strDate.split("/", 0);

        for (int i = 0; i < values.length; i++) {

            dateText = (values[0]);
            timeStamp = (values[1]);
        }

        mDbHelper = new WaterDbHelper(getApplicationContext());

        //ImageView glass = findViewById(R.id.glass);

        //Button hydrate = findViewById(R.id.buttonh);

        drinkCounts = findViewById(R.id.drinkCount);


        drinkCounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent incrementWaterCountIntent = new Intent(MainActivity.this, WaterReminderIntentService.class);
                incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
                startService(incrementWaterCountIntent);

                //saveNumberOfCups();
            }
        });


        // Kick off the loader
        getLoaderManager().initLoader(PET_LOADER, null, this);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_history) {
            Intent HistoryIntent = new Intent(this, History.class);
            startActivity(HistoryIntent);
            return true;
        } if(id==R.id.action_increment){

            Intent incrementWaterCountIntent = new Intent(MainActivity.this, WaterReminderIntentService.class);
            incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
            startService(incrementWaterCountIntent);

        } if(id==R.id.delete){

            Intent incrementWaterCountIntent = new Intent(MainActivity.this, WaterReminderIntentService.class);
            incrementWaterCountIntent.setAction(ReminderTasks.ACTION_DECREMENT_WATER_COUNT);
            startService(incrementWaterCountIntent);

        }if(id==R.id.action_averagecups){

            Intent avgIntent = new Intent (this, AverageActivity.class);
            startActivity(avgIntent);
        }


        return super.onOptionsItemSelected(item);
    }

    /*private void saveNumberOfCups() {

        dateText = "0";
        timeStamp = "1";


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy/HH:mm:ss", Locale.US);
        String strDate = sdf.format(cal.getTime());
        String[] values = strDate.split("/", 0);

        for (int i = 0; i < values.length; i++) {

            dateText = (values[0]);
            timeStamp = (values[1]);
        }

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {WaterContract.WaterEntry._ID,
                WaterContract.WaterEntry.COLUMN_TOTALCUPS,
                WaterContract.WaterEntry.COLUMN_DATE};

        Cursor cursor = database.query(WaterContract.WaterEntry.TABLE_NAME, projection, null, null,
                null, null, null);
        Log.v(TAG, "Database Query " + cursor.getCount());

        if (cursor.moveToLast()) {

            int cupColumnIndex = cursor.getColumnIndex(WaterContract.WaterEntry.COLUMN_DATE);
            date = cursor.getString(cupColumnIndex);
            int ids = cursor.getColumnIndex(WaterContract.WaterEntry._ID);
            idColumn = cursor.getLong(ids);
            int numberOfCupsIndex = cursor.getColumnIndex(WaterContract.WaterEntry.COLUMN_TOTALCUPS);
            int numberOfCups = cursor.getInt(numberOfCupsIndex);
            currentNumberDrinks = numberOfCups + 1;
            Log.v(TAG, "Cursor move to last is called in Save cups " + currentNumberDrinks + date);
            cursor.close();

            }

         if (date.equals(dateText)) {


                ContentValues contentvalues = new ContentValues();
                contentvalues.put(WaterContract.WaterEntry.COLUMN_DATE, dateText);
                contentvalues.put(WaterContract.WaterEntry.COLUMN_TIME, timeStamp);
                contentvalues.put(WaterContract.WaterEntry.COLUMN_TOTALCUPS, currentNumberDrinks);

                Log.v(TAG, "Cursor move to same date loop in Save cups " + dateText + timeStamp +   currentNumberDrinks);

                Uri currentPetUri = ContentUris.withAppendedId(WaterContract.WaterEntry.CONTENT_URI, idColumn);
                int rowsAffected = getContentResolver().update(currentPetUri, contentvalues, null, null);


            } else {
                currentNumberDrinks=1;
                ContentValues contentvalues = new ContentValues();
                contentvalues.put(WaterContract.WaterEntry.COLUMN_DATE, dateText);
                contentvalues.put(WaterContract.WaterEntry.COLUMN_TIME, timeStamp);
                contentvalues.put(WaterContract.WaterEntry.COLUMN_TOTALCUPS, currentNumberDrinks);

                Log.v(TAG, "Cursor move to different date loop in Save cups " + dateText + timeStamp +   currentNumberDrinks);

                // Get writeable database
                SQLiteDatabase writabledatabase = mDbHelper.getWritableDatabase();

             Uri newUri = getContentResolver().insert(WaterContract.WaterEntry.CONTENT_URI, contentvalues);

            }
        }*/

        @Override
        public Loader<Cursor> onCreateLoader ( int i, Bundle bundle){

            // Define a projection that specifies the columns from the table we care about.
            String[] projection = {
                    WaterContract.WaterEntry.COLUMN_TOTALCUPS};

            // This loader will execute the ContentProvider's query method on a background thread
            return new CursorLoader(this,   // Parent activity context
                    WaterContract.WaterEntry.CONTENT_URI,   // Provider content URI to query
                    projection,             // Columns to include in the resulting Cursor
                    null,                   // No selection clause
                    null,                   // No selection arguments
                    null);                  // Default sort order
        }

        @Override
        public void onLoadFinished (Loader < Cursor > loader, Cursor data){
            //drinkCounts = findViewById(R.id.drinkCount);



            if (data == null || data.getCount() < 1) {
                Log.v(TAG, "Data is null in Load Finished " + numberOfCups);
                ContentValues contentvalues = new ContentValues();
                contentvalues.put(WaterContract.WaterEntry.COLUMN_DATE, dateText);
                contentvalues.put(WaterContract.WaterEntry.COLUMN_TIME, timeStamp);
                contentvalues.put(WaterContract.WaterEntry.COLUMN_TOTALCUPS, 0);

                // Get writeable database
                SQLiteDatabase writabledatabase = mDbHelper.getWritableDatabase();

                //long id = writabledatabase.insert(WaterContract.WaterEntry.TABLE_NAME, null, contentvalues);
                Uri newUri = getContentResolver().insert(WaterContract.WaterEntry.CONTENT_URI, contentvalues);

            }
            if (data.moveToLast()) {
                int cupColumnIndex = data.getColumnIndex(WaterContract.WaterEntry.COLUMN_TOTALCUPS);
                numberOfCups = data.getInt(cupColumnIndex);
                Log.v(TAG, "Move to Last in Load Finished " + numberOfCups);
                // Update {@link PetCursorAdapter} with this new cursor containing updated pet data
                drinkCounts.setText(Integer.toString(numberOfCups));
            }

        }

        @Override
        public void onLoaderReset (Loader < Cursor > loader) {
            // Callback called when the data needs to be deleted
            drinkCounts.setText("");
        }

    public void onBackPressed(){
        super.onBackPressed();
        WaterCountNotificationUtilities.clearAllNotifications(this);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        WaterCountNotificationUtilities.clearAllNotifications(this);

    }



    }
