package com.example.ganga.drinkwater.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ganga on 4/29/18.
 */

public class WaterDbHelper extends SQLiteOpenHelper{

    public static final String LOG_TAG = WaterDbHelper.class.getSimpleName();

    /**
     * Database helper for Pets app. Manages database creation and version management.
     */

        /** Name of the database file */
        private static final String DATABASE_NAME = "drinkwater.db";

        /**
         * Database version. If you change the database schema, you must increment the database version.
         */
        private static final int DATABASE_VERSION = 1;

        /**
         * Constructs a new instance of {@link WaterDbHelper}.
         *
         * @param context of the app
         */
        public WaterDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * This is called when the database is created for the first time.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create a String that contains the SQL statement to create the pets table
            String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + WaterContract.WaterEntry.TABLE_NAME + " ("
                    + WaterContract.WaterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + WaterContract.WaterEntry.COLUMN_DATE + " TEXT NOT NULL, "
                    + WaterContract.WaterEntry.COLUMN_TIME + " TEXT NOT NULL, "
                    + WaterContract.WaterEntry.COLUMN_TOTALCUPS + " INTEGER NOT NULL);,";
            // Execute the SQL statement
            db.execSQL(SQL_CREATE_PETS_TABLE);
        }

        /**
         * This is called when the database needs to be upgraded.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // The database is still at version 1, so there's nothing to do be done here.
        }


    }

