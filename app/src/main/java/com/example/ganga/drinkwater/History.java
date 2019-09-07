package com.example.ganga.drinkwater;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ganga.drinkwater.data.WaterContract;
import com.example.ganga.drinkwater.data.WaterDbHelper;
import com.example.ganga.drinkwater.utilities.WaterCountNotificationUtilities;

public class History extends AppCompatActivity {

    private RecyclerView mNumbersList;
    private CupsAdapter mAdapter;
    private WaterDbHelper mDbHelper;
    Cursor mcursor;

    /**
     * Identifier for the pet data loader
     */
    private static final int PET_LOADER1 = 1;

    private final String TAG = History.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // The following line presents the back arrow button on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        WaterCountNotificationUtilities.clearAllNotifications(this);

        mDbHelper = new WaterDbHelper(getApplicationContext());
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Log.v(TAG, "mDbHelper is Called");

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {WaterContract.WaterEntry._ID,
                WaterContract.WaterEntry.COLUMN_TOTALCUPS,
                WaterContract.WaterEntry.COLUMN_DATE};

       mcursor = database.query(WaterContract.WaterEntry.TABLE_NAME, projection, null, null,
                null, null, WaterContract.WaterEntry._ID + " DESC");

         /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mNumbersList = (RecyclerView) findViewById(R.id.rv_numbers);

         /*
         * A LinearLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a linear list. This means that it can produce either a horizontal or
         * vertical list depending on which parameter you pass in to the LinearLayoutManager
         * constructor. By default, if you don't specify an orientation, you get a vertical list.
         * In our case, we want a vertical list, so we don't need to pass in an orientation flag to
         * the LinearLayoutManager constructor.
         *
         * There are other LayoutManagers available to display your data in uniform grids,
         * staggered grids, and more! See the developer documentation for more details.
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNumbersList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mNumbersList.getContext(),
                layoutManager.getOrientation());
        mNumbersList.addItemDecoration(dividerItemDecoration);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(History.this, R.drawable.divider_drawable));

         /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mNumbersList.setHasFixedSize(true);

          /*
         * The GreenAdapter is responsible for displaying each item in the list.
         */
        mAdapter = new CupsAdapter(this, mcursor);

        Log.v(TAG, "Adapter Called");

        mNumbersList.setAdapter(mAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        WaterCountNotificationUtilities.clearAllNotifications(this);

    }



}
