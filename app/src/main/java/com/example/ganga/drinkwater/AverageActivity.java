package com.example.ganga.drinkwater;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.ganga.drinkwater.data.WaterContract;
import com.example.ganga.drinkwater.data.WaterDbHelper;
import com.example.ganga.drinkwater.utilities.WaterCountNotificationUtilities;

public class AverageActivity extends AppCompatActivity {


    private WaterDbHelper mDbHelper;

    private final String TAG = History.class.getSimpleName();

    Cursor mcursor;
    int noOfRows;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average);

        // The following line presents the back arrow button on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDbHelper = new WaterDbHelper(getApplicationContext());

        int totalcups = getSold();
        int average = (totalcups/noOfRows);
        String avgToShow = Integer.toString(average);

        TextView avgtextView = (TextView) findViewById(R.id.textView);
        avgtextView.setText(avgToShow);

    }


    public int getSold() {
        int sold = 0;
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        mcursor = database.query(WaterContract.WaterEntry.TABLE_NAME, null, null, null, null, null, null);
        noOfRows = mcursor.getCount();

        if (mcursor.moveToFirst()) {
            while (!mcursor.isAfterLast()) {
                sold += mcursor.getInt(mcursor.getColumnIndex(WaterContract.WaterEntry.COLUMN_TOTALCUPS));
                mcursor.moveToNext();
            }
            mcursor.close();
        }
        database.close();
        return sold;
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
