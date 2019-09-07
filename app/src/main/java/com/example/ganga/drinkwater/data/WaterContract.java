package com.example.ganga.drinkwater.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ganga on 4/29/18.
 */

public class WaterContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private WaterContract() {

    }

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.ganga.drinkwater";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.inventory/inventory/ is a valid path for
     * looking at inventory data. content://com.example.android.inventory/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_WATERCOUNT = "cups";


    /**
     * Inner class that defines constant values for the inventory database table thats created by the seller.
     * Each entry in the table represents a single inventory.
     */

    public static final class WaterEntry implements BaseColumns {

        /** The content URI to access the sellers inventory data in the provider */

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_WATERCOUNT);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items on cups table.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WATERCOUNT;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item on cups table.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WATERCOUNT;

        /** Name of database table for inventory */

        public final static String TABLE_NAME = "cups";


        public final static String _ID = BaseColumns._ID;


        public final static String COLUMN_DATE ="date";

        public final static String COLUMN_TIME="time";

        public final static String COLUMN_TOTALCUPS = "numberofcups";

    }

}
